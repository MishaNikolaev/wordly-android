package com.nmichail.wordly.android.features.materials.presentation.detail

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labelsChannel
import com.nmichail.wordly.android.component.presentation.launchTry
import com.nmichail.wordly.android.core.navigation.asValue

internal class DefaultMaterialDetailComponent(
	componentContext: ComponentContext,
	materialId: String,
	materialDetailStoreFactory: MaterialDetailStoreFactory,
	private val materialDetailRouter: MaterialDetailRouter,
) : ComponentContext by componentContext,
	MaterialDetailComponent {

	private val store: MaterialDetailStore = instanceKeeper.getStore {
		materialDetailStoreFactory.create(materialId = materialId)
	}

	override val model: Value<MaterialDetailComponent.State> = store.asValue()

	init {
		launchTry {
			for (label in store.labelsChannel(lifecycle)) {
				when (label) {
					MaterialDetailComponent.Label.Close -> materialDetailRouter.navigateBack()
					is MaterialDetailComponent.Label.Share -> Unit
				}
			}
		} catch {
			// ignored
		}
	}

	override fun handleBack() {
		store.accept(MaterialDetailStore.Intent.Back)
	}

	override fun handleRetry() {
		store.accept(MaterialDetailStore.Intent.Retry)
	}

	override fun handleShare() {
		store.accept(MaterialDetailStore.Intent.Share)
	}

	override fun handleLike() {
		store.accept(MaterialDetailStore.Intent.Like)
	}

	override fun handleDislike() {
		store.accept(MaterialDetailStore.Intent.Dislike)
	}
}