package com.nmichail.wordly.android.features.materials.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.nmichail.wordly.android.component.presentation.BaseCoroutineExecutor
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialFilter
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialItem
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialReadStatus
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialsCatalog
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialsFilters
import com.nmichail.wordly.android.features.materials.domain.usecase.GetMaterialsUseCase
import javax.inject.Inject

internal class MaterialsStoreFactory @Inject constructor(
	private val getMaterialsUseCase: GetMaterialsUseCase,
) {

	private val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory())

	fun create(): MaterialsStore =
		object :
			MaterialsStore,
			Store<MaterialsStore.Intent, MaterialsComponent.State, MaterialsComponent.Label> by storeFactory.create(
				name = "MaterialsStore",
				initialState = MaterialsComponent.State.Loading,
				bootstrapper = SimpleBootstrapper(Action.Load),
				executorFactory = ::ExecutorImpl,
				reducer = ReducerImpl,
			) {}

	private sealed interface Action {

		data object Load : Action
	}

	private sealed interface Msg {

		data object Loading : Msg

		data object SetError : Msg

		data class CatalogLoaded(
			val catalog: MaterialsCatalog,
			val filter: MaterialFilter,
		) : Msg

		data class FilterUpdated(
			val filter: MaterialFilter,
			val items: List<MaterialItem>,
		) : Msg
	}

	private object ReducerImpl : Reducer<MaterialsComponent.State, Msg> {

		override fun MaterialsComponent.State.reduce(msg: Msg): MaterialsComponent.State =
			when (msg) {
				Msg.Loading -> MaterialsComponent.State.Loading
				Msg.SetError -> MaterialsComponent.State.Error
				is Msg.CatalogLoaded -> MaterialsComponent.State.Content(
					title = msg.catalog.title,
					selectedFilter = msg.filter,
					items = msg.catalog.items,
				)
				is Msg.FilterUpdated -> {
					val content = this as? MaterialsComponent.State.Content ?: return this
					content.copy(
						selectedFilter = msg.filter,
						items = msg.items,
					)
				}
			}
	}

	private inner class ExecutorImpl :
		BaseCoroutineExecutor<
			MaterialsStore.Intent,
			Action,
			MaterialsComponent.State,
			Msg,
			MaterialsComponent.Label,
			>() {

		private val locallyReadIds = mutableSetOf<String>()

		override fun executeAction(action: Action) {
			when (action) {
				Action.Load -> load(showLoading = true)
			}
		}

		override fun executeIntent(intent: MaterialsStore.Intent) {
			when (intent) {
				MaterialsStore.Intent.Retry -> load(showLoading = true)
				is MaterialsStore.Intent.ChangeFilter -> {
					load(filter = intent.filter, showLoading = false)
				}
				is MaterialsStore.Intent.OpenMaterial -> {
					val content = state() as? MaterialsComponent.State.Content ?: return
					val material = content.items.find { it.id == intent.materialId } ?: return
					val openedMaterial = if (material.status == MaterialReadStatus.New) {
						locallyReadIds += material.id
						val updated = material.copy(status = MaterialReadStatus.Read)
						dispatch(
							Msg.FilterUpdated(
								filter = content.selectedFilter,
								items = content.items.map { item ->
									if (item.id == updated.id) updated else item
								},
							),
						)
						updated
					} else {
						material
					}
					publish(MaterialsComponent.Label.OpenMaterial(material = openedMaterial))
				}
			}
		}

		private fun load(
			filter: MaterialFilter = (state() as? MaterialsComponent.State.Content)?.selectedFilter
				?: MaterialFilter.All,
			showLoading: Boolean,
		) {
			if (showLoading) {
				dispatch(Msg.Loading)
			}
			launchTry {
				val catalog = getMaterialsUseCase(filters = MaterialsFilters(filter = filter))
				val items = catalog.items.map { item ->
					if (item.id in locallyReadIds) {
						item.copy(status = MaterialReadStatus.Read)
					} else {
						item
					}
				}
				if (showLoading) {
					dispatch(
						Msg.CatalogLoaded(
							catalog = catalog.copy(items = items),
							filter = filter,
						),
					)
				} else {
					dispatch(
						Msg.FilterUpdated(
							filter = filter,
							items = items,
						),
					)
				}
			} catch {
				if (showLoading) {
					dispatch(Msg.SetError)
				}
			}
		}
	}
}