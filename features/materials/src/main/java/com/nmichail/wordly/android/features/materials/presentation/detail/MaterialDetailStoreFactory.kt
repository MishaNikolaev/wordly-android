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
				MaterialDetailStore.State,
				MaterialDetailStore.Label,
				> by storeFactory.create(
				name = "MaterialDetailStore",
				initialState = MaterialDetailStore.State.Loading,
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

	private object ReducerImpl : Reducer<MaterialDetailStore.State, Msg> {

		override fun MaterialDetailStore.State.reduce(msg: Msg): MaterialDetailStore.State {
			val content = this as? MaterialDetailStore.State.Content
			return when (msg) {
				Msg.Loading -> MaterialDetailStore.State.Loading
				Msg.SetError -> MaterialDetailStore.State.Error
				is Msg.Loaded -> MaterialDetailStore.State.Content(
					material = msg.material,
					selectedReaction = null,
				)
				is Msg.ReactionUpdated -> content?.copy(selectedReaction = msg.reaction) ?: this
			}
		}
	}

	private inner class ExecutorImpl(
		private val materialId: String,
	) : BaseCoroutineExecutor<
		MaterialDetailStore.Intent,
		Action,
		MaterialDetailStore.State,
		Msg,
		MaterialDetailStore.Label,
		>() {

		override fun executeAction(action: Action) {
			when (action) {
				is Action.Load -> load(materialId = action.materialId)
			}
		}

		override fun executeIntent(intent: MaterialDetailStore.Intent) {
			when (intent) {
				MaterialDetailStore.Intent.Retry -> load(materialId = materialId)
				MaterialDetailStore.Intent.Back -> publish(MaterialDetailStore.Label.Close)
				MaterialDetailStore.Intent.Share -> {
					val content = state() as? MaterialDetailStore.State.Content ?: return
					publish(MaterialDetailStore.Label.Share(title = content.material.title))
				}
				MaterialDetailStore.Intent.Like -> toggleReaction(MaterialReaction.Like)
				MaterialDetailStore.Intent.Dislike -> toggleReaction(MaterialReaction.Dislike)
			}
		}

		private fun toggleReaction(reaction: MaterialReaction) {
			val content = state() as? MaterialDetailStore.State.Content ?: return
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