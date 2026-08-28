package com.nmichail.wordly.android.features.materials.article.presentation

import kotlinx.coroutines.launch
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.nmichail.wordly.android.component.presentation.BaseCoroutineExecutor
import com.nmichail.wordly.android.features.materials.article.domain.entity.MaterialDetail
import com.nmichail.wordly.android.features.materials.article.domain.entity.MaterialReaction
import com.nmichail.wordly.android.features.materials.article.domain.usecase.GetMaterialReactionUseCase
import com.nmichail.wordly.android.features.materials.article.domain.usecase.GetMaterialUseCase
import com.nmichail.wordly.android.features.materials.article.domain.usecase.SetMaterialReactionUseCase
import javax.inject.Inject

internal class MaterialDetailStoreFactory @Inject constructor(
	private val getMaterialUseCase: GetMaterialUseCase,
	private val getMaterialReactionUseCase: GetMaterialReactionUseCase,
	private val setMaterialReactionUseCase: SetMaterialReactionUseCase,
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
				initialState = MaterialDetailStore.State.Initial,
				bootstrapper = SimpleBootstrapper(Action.Init(materialId = materialId)),
				executorFactory = { ExecutorImpl(materialId = materialId) },
				reducer = ReducerImpl,
			) {}

	private sealed interface Action {

		data class Init(val materialId: String) : Action
	}

	private sealed interface Msg {

		data object Loading : Msg

		data object SetError : Msg

		data class Loaded(
			val material: MaterialDetail,
			val selectedReaction: MaterialReaction?,
		) : Msg

		data class ReactionUpdated(
			val material: MaterialDetail,
			val reaction: MaterialReaction?,
		) : Msg
	}

	private object ReducerImpl : Reducer<MaterialDetailStore.State, Msg> {

		override fun MaterialDetailStore.State.reduce(msg: Msg): MaterialDetailStore.State {
			val content = this as? MaterialDetailStore.State.Content
			return when (msg) {
				Msg.Loading -> MaterialDetailStore.State.Loading
				Msg.SetError -> MaterialDetailStore.State.Error
				is Msg.Loaded -> MaterialDetailStore.State.Content(
					material = msg.material,
					selectedReaction = msg.selectedReaction,
				)
				is Msg.ReactionUpdated -> content?.copy(
					material = msg.material,
					selectedReaction = msg.reaction,
				) ?: this
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
				is Action.Init -> load(materialId = action.materialId)
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
			val optimistic = content.material.withOptimisticReaction(
				previous = content.selectedReaction,
				next = next,
			)
			dispatch(Msg.ReactionUpdated(material = optimistic, reaction = next))
			scope.launch {
				val updated = setMaterialReactionUseCase(id = materialId, reaction = next)
				dispatch(Msg.ReactionUpdated(material = updated, reaction = next))
			}
		}

		private fun load(materialId: String) {
			dispatch(Msg.Loading)
			scope.launch {
				try {
					val material = getMaterialUseCase(materialId)
					val reaction = getMaterialReactionUseCase(materialId)
					dispatch(Msg.Loaded(material = material, selectedReaction = reaction))
				} catch (_: Exception) {
					dispatch(Msg.SetError)
				}
			}
		}
	}
}

private fun MaterialDetail.withOptimisticReaction(
	previous: MaterialReaction?,
	next: MaterialReaction?,
): MaterialDetail {
	var likes = likes
	var dislikes = dislikes
	when (previous) {
		MaterialReaction.Like -> likes -= 1
		MaterialReaction.Dislike -> dislikes -= 1
		null -> Unit
	}
	when (next) {
		MaterialReaction.Like -> likes += 1
		MaterialReaction.Dislike -> dislikes += 1
		null -> Unit
	}
	return copy(
		likes = likes.coerceAtLeast(0),
		dislikes = dislikes.coerceAtLeast(0),
	)
}
