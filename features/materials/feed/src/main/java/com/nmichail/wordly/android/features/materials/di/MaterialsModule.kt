package com.nmichail.wordly.android.features.materials.di

import com.nmichail.wordly.android.core.network.di.GeneralRetrofit
import com.nmichail.wordly.android.features.materials.data.api.MaterialsApi
import com.nmichail.wordly.android.features.materials.data.datasource.MaterialsDataSource
import com.nmichail.wordly.android.features.materials.data.datasource.MaterialsDataSourceImpl
import com.nmichail.wordly.android.features.materials.data.repository.MaterialsRepositoryImpl
import com.nmichail.wordly.android.features.materials.domain.repository.MaterialsRepository
import com.nmichail.wordly.android.features.materials.presentation.DefaultMaterialsComponentFactory
import com.nmichail.wordly.android.features.materials.presentation.MaterialsComponent
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
abstract class MaterialsModule {

	@Binds
	abstract fun bindMaterialsDataSource(
		impl: MaterialsDataSourceImpl,
	): MaterialsDataSource

	@Binds
	abstract fun bindMaterialsRepository(
		impl: MaterialsRepositoryImpl,
	): MaterialsRepository

	@Binds
	internal abstract fun bindMaterialsComponentFactory(
		impl: DefaultMaterialsComponentFactory,
	): MaterialsComponent.Factory

	companion object {

		@Provides
		@Singleton
		fun provideMaterialsApi(
			@GeneralRetrofit retrofit: Retrofit,
		): MaterialsApi =
			retrofit.create(MaterialsApi::class.java)
	}
}
