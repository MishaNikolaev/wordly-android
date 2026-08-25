package com.nmichail.wordly.android.features.profile.editor.di

import com.nmichail.wordly.android.core.network.di.GeneralRetrofit
import com.nmichail.wordly.android.features.profile.editor.data.api.ProfileEditApi
import com.nmichail.wordly.android.features.profile.editor.data.datasource.ProfileEditDataSource
import com.nmichail.wordly.android.features.profile.editor.data.datasource.ProfileEditDataSourceImpl
import com.nmichail.wordly.android.features.profile.editor.data.repository.ProfileEditRepositoryImpl
import com.nmichail.wordly.android.features.profile.editor.domain.repository.ProfileEditRepository
import com.nmichail.wordly.android.features.profile.editor.presentation.DefaultProfileEditComponentFactory
import com.nmichail.wordly.android.features.profile.editor.presentation.ProfileEditComponent
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
abstract class ProfileEditModule {

	@Binds
	abstract fun bindProfileEditDataSource(
		impl: ProfileEditDataSourceImpl,
	): ProfileEditDataSource

	@Binds
	abstract fun bindProfileEditRepository(
		impl: ProfileEditRepositoryImpl,
	): ProfileEditRepository

	@Binds
	internal abstract fun bindProfileEditComponentFactory(
		impl: DefaultProfileEditComponentFactory,
	): ProfileEditComponent.Factory

	companion object {

		@Provides
		@Singleton
		fun provideProfileEditApi(
			@GeneralRetrofit retrofit: Retrofit,
		): ProfileEditApi =
			retrofit.create(ProfileEditApi::class.java)
	}
}
