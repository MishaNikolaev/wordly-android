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
			Store<MaterialsStore.Intent, MaterialsStore.State, MaterialsStore.Label> by storeFactory.create(
				name = "MaterialsStore",
				initialState = MaterialsStore.State.Loading,
				bootstrapper = SimpleBootstrapper(Action.Load),
				executorFactory = ::ExecutorImpl,
				reducer = ReducerImpl,
			) {}

	private sealed interface Action {

		data object Load : Action
	}

	private sealed interface Msg {

		data object Loading : Msg

		data class SetError(
			val locallyReadIds: Set<String>,
		) : Msg

		data class CatalogLoaded(
			val catalog: MaterialsCatalog,
			val filter: MaterialFilter,
			val locallyReadIds: Set<String>,
		) : Msg

		data class FilterUpdated(
			val filter: MaterialFilter,
			val items: List<MaterialItem>,
			val locallyReadIds: Set<String>,
		) : Msg
	}

	private object ReducerImpl : Reducer<MaterialsStore.State, Msg> {

		override fun MaterialsStore.State.reduce(msg: Msg): MaterialsStore.State {
			val content = this as? MaterialsStore.State.Content
			return when (msg) {
				Msg.Loading -> MaterialsStore.State.Loading
				is Msg.SetError -> MaterialsStore.State.Error(
					locallyReadIds = msg.locallyReadIds,
				)
				is Msg.CatalogLoaded -> MaterialsStore.State.Content(
					title = msg.catalog.title,
					selectedFilter = msg.filter,
					items = msg.catalog.items,
					locallyReadIds = msg.locallyReadIds,
				)
				is Msg.FilterUpdated -> content?.copy(
					selectedFilter = msg.filter,
					items = msg.items,
					locallyReadIds = msg.locallyReadIds,
				) ?: this
			}
		}
	}

	private inner class ExecutorImpl :
		BaseCoroutineExecutor<
			MaterialsStore.Intent,
			Action,
			MaterialsStore.State,
			Msg,
			MaterialsStore.Label,
			>() {

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
				is MaterialsStore.Intent.OpenMaterial -> openMaterial(materialId = intent.materialId)
			}
		}

		private fun openMaterial(materialId: String) {
			val content = state() as? MaterialsStore.State.Content ?: return
			val material = content.items.find { it.id == materialId } ?: return
			val openedMaterial = if (material.status == MaterialReadStatus.New) {
				val updated = material.copy(status = MaterialReadStatus.Read)
				val locallyReadIds = content.locallyReadIds + material.id
				dispatch(
					Msg.FilterUpdated(
						filter = content.selectedFilter,
						items = content.items.map { item ->
							if (item.id == updated.id) updated else item
						},
						locallyReadIds = locallyReadIds,
					),
				)
				updated
			} else {
				material
			}
			publish(MaterialsStore.Label.OpenMaterial(material = openedMaterial))
		}

		private fun load(
			filter: MaterialFilter = (state() as? MaterialsStore.State.Content)?.selectedFilter
				?: MaterialFilter.All,
			showLoading: Boolean,
		) {
			val locallyReadIds = currentLocallyReadIds()
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
							locallyReadIds = locallyReadIds,
						),
					)
				} else {
					dispatch(
						Msg.FilterUpdated(
							filter = filter,
							items = items,
							locallyReadIds = locallyReadIds,
						),
					)
				}
			} catch {
				if (showLoading) {
					dispatch(Msg.SetError(locallyReadIds = locallyReadIds))
				}
			}
		}

		private fun currentLocallyReadIds(): Set<String> =
			when (val current = state()) {
				is MaterialsStore.State.Content -> current.locallyReadIds
				is MaterialsStore.State.Error -> current.locallyReadIds
				MaterialsStore.State.Loading -> emptySet()
			}
	}
}