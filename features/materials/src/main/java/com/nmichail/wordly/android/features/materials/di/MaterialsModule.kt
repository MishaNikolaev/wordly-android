package com.nmichail.wordly.android.features.materials.di

import com.nmichail.wordly.android.core.network.di.GeneralRetrofit
import com.nmichail.wordly.android.features.materials.data.api.MaterialsApi
import com.nmichail.wordly.android.features.materials.data.repository.MaterialsRepositoryImpl
import com.nmichail.wordly.android.features.materials.domain.repository.MaterialsRepository
import com.nmichail.wordly.android.features.materials.presentation.DefaultMaterialsComponentFactory
import com.nmichail.wordly.android.features.materials.presentation.MaterialsComponent
import com.nmichail.wordly.android.features.materials.presentation.detail.DefaultMaterialDetailComponentFactory
import com.nmichail.wordly.android.features.materials.presentation.detail.MaterialDetailComponent
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
abstract class MaterialsModule {

	@Binds
	abstract fun bindMaterialsRepository(
		impl: MaterialsRepositoryImpl,
	): MaterialsRepository

	@Binds
	internal abstract fun bindMaterialsComponentFactory(
		impl: DefaultMaterialsComponentFactory,
	): MaterialsComponent.Factory

	@Binds
	internal abstract fun bindMaterialDetailComponentFactory(
		impl: DefaultMaterialDetailComponentFactory,
	): MaterialDetailComponent.Factory

	companion object {

		@Provides
		@Singleton
		fun provideMaterialsApi(
			@GeneralRetrofit retrofit: Retrofit,
		): MaterialsApi =
			retrofit.create(MaterialsApi::class.java)
	}
}