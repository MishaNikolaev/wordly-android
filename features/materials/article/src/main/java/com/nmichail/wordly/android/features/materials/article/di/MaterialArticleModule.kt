package com.nmichail.wordly.android.features.materials.article.di

import com.nmichail.wordly.android.core.network.di.GeneralRetrofit
import com.nmichail.wordly.android.features.materials.article.data.api.MaterialArticleApi
import com.nmichail.wordly.android.features.materials.article.data.datasource.MaterialArticleDataSource
import com.nmichail.wordly.android.features.materials.article.data.datasource.MaterialArticleDataSourceImpl
import com.nmichail.wordly.android.features.materials.article.data.repository.MaterialArticleRepositoryImpl
import com.nmichail.wordly.android.features.materials.article.domain.repository.MaterialArticleRepository
import com.nmichail.wordly.android.features.materials.article.presentation.DefaultMaterialDetailComponent
import com.nmichail.wordly.android.features.materials.article.presentation.MaterialDetailComponent
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
abstract class MaterialArticleModule {

	@Binds
	abstract fun bindMaterialArticleRepository(
		impl: MaterialArticleRepositoryImpl,
	): MaterialArticleRepository

	@Binds
	abstract fun bindMaterialArticleDataSource(
		impl: MaterialArticleDataSourceImpl,
	): MaterialArticleDataSource

	@Binds
	internal abstract fun bindMaterialDetailComponentFactory(
		impl: DefaultMaterialDetailComponent.Factory,
	): MaterialDetailComponent.Factory

	companion object {

		@Provides
		@Singleton
		fun provideMaterialArticleApi(
			@GeneralRetrofit retrofit: Retrofit,
		): MaterialArticleApi =
			retrofit.create(MaterialArticleApi::class.java)
	}
}
