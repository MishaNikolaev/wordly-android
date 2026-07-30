package com.nmichail.wordly.android.features.materials.presentation.detail

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.nmichail.wordly.android.component.presentation.BaseCoroutineExecutor
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialDetail
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialReaction
import com.nmichail.wordly.android.features.materials.domain.usecase.GetMaterialUseCase
import javax.inject.Inject

internal class MaterialDetailStoreFactory @Inject constructor(
	private val getMaterialUseCase: GetMaterialUseCase,
) {

	private val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory())

	fun create(materialId: String): MaterialDetailStore =
		object :
			MaterialDetailStore,
			Store<
				MaterialDetailStore.Intent,
				MaterialDetailComponent.State,
				MaterialDetailComponent.Label,
				> by storeFactory.create(
				name = "MaterialDetailStore",
				initialState = MaterialDetailComponent.State.Loading,
				bootstrapper = SimpleBootstrapper(Action.Load(materialId = materialId)),
				executorFactory = { ExecutorImpl(materialId = materialId) },
				reducer = ReducerImpl,
			) {}

	private sealed interface Action {

		data class Load(val materialId: String) : Action
	}

	private sealed interface Msg {

		data object Loading : Msg

		data object SetError : Msg

		data class Loaded(val material: MaterialDetail) : Msg

		data class ReactionUpdated(val reaction: MaterialReaction?) : Msg
	}

	private object ReducerImpl : Reducer<MaterialDetailComponent.State, Msg> {

		override fun MaterialDetailComponent.State.reduce(
			msg: Msg,
		): MaterialDetailComponent.State =
			when (msg) {
				Msg.Loading -> MaterialDetailComponent.State.Loading
				Msg.SetError -> MaterialDetailComponent.State.Error
				is Msg.Loaded -> MaterialDetailComponent.State.Content(
					material = msg.material,
					selectedReaction = null,
				)
				is Msg.ReactionUpdated -> {
					val content = this as? MaterialDetailComponent.State.Content ?: return this
					content.copy(selectedReaction = msg.reaction)
				}
			}
	}

	private inner class ExecutorImpl(
		private val materialId: String,
	) : BaseCoroutineExecutor<
		MaterialDetailStore.Intent,
		Action,
		MaterialDetailComponent.State,
		Msg,
		MaterialDetailComponent.Label,
		>() {

		override fun executeAction(action: Action) {
			when (action) {
				is Action.Load -> load(materialId = action.materialId)
			}
		}

		override fun executeIntent(intent: MaterialDetailStore.Intent) {
			when (intent) {
				MaterialDetailStore.Intent.Retry -> load(materialId = materialId)
				MaterialDetailStore.Intent.Back -> publish(MaterialDetailComponent.Label.Close)
				MaterialDetailStore.Intent.Share -> {
					val content = state() as? MaterialDetailComponent.State.Content ?: return
					publish(MaterialDetailComponent.Label.Share(title = content.material.title))
				}
				MaterialDetailStore.Intent.Like -> toggleReaction(MaterialReaction.Like)
				MaterialDetailStore.Intent.Dislike -> toggleReaction(MaterialReaction.Dislike)
			}
		}

		private fun toggleReaction(reaction: MaterialReaction) {
			val content = state() as? MaterialDetailComponent.State.Content ?: return
			val next = if (content.selectedReaction == reaction) null else reaction
			dispatch(Msg.ReactionUpdated(reaction = next))
		}

		private fun load(materialId: String) {
			dispatch(Msg.Loading)
			launchTry {
				val material = getMaterialUseCase(id = materialId)
				dispatch(Msg.Loaded(material = material))
			} catch {
				dispatch(Msg.SetError)
			}
		}
	}
}