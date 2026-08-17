package com.nmichail.wordly.android.features.materials.presentation

import com.nmichail.wordly.android.core.navigation.componentScope
import kotlinx.coroutines.launch
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labelsChannel
import com.nmichail.wordly.android.core.navigation.asValue
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialFilter
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialItem

internal class DefaultMaterialsComponent(
    componentContext: ComponentContext,
    materialsStoreFactory: MaterialsStoreFactory,
    private val onMaterialClick: (MaterialItem) -> Unit,
) : ComponentContext by componentContext,
    MaterialsComponent {

    private val store: MaterialsStore = instanceKeeper.getStore {
        materialsStoreFactory.create()
    }

    override val model: Value<MaterialsStore.State> = store.asValue()

    init {
        componentScope().launch {
            for (label in store.labelsChannel(lifecycle)) {
                when (label) {
                    is MaterialsStore.Label.OpenMaterial -> onMaterialClick(label.material)
                }
            }
        }
    }

    override fun handleRetry() {
        store.accept(MaterialsStore.Intent.Retry)
    }

    override fun handleFilterChange(filter: MaterialFilter) {
        store.accept(MaterialsStore.Intent.ChangeFilter(filter = filter))
    }

    override fun handleOpenMaterial(materialId: String) {
        store.accept(MaterialsStore.Intent.OpenMaterial(materialId = materialId))
    }
}
