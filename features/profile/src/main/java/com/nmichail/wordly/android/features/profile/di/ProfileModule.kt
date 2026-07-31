package com.nmichail.wordly.android.features.profile.di

import com.nmichail.wordly.android.core.network.di.GeneralRetrofit
import com.nmichail.wordly.android.features.profile.data.api.ProfileApi
import com.nmichail.wordly.android.features.profile.data.repository.ProfileRepositoryImpl
import com.nmichail.wordly.android.features.profile.domain.repository.ProfileRepository
import com.nmichail.wordly.android.features.profile.presentation.DefaultProfileComponentFactory
import com.nmichail.wordly.android.features.profile.presentation.ProfileComponent
import com.nmichail.wordly.android.features.profile.presentation.edit.DefaultProfileEditComponentFactory
import com.nmichail.wordly.android.features.profile.presentation.edit.ProfileEditComponent
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
abstract class ProfileModule {

	@Binds
	abstract fun bindProfileRepository(
		impl: ProfileRepositoryImpl,
	): ProfileRepository

	@Binds
	internal abstract fun bindProfileComponentFactory(
		impl: DefaultProfileComponentFactory,
	): ProfileComponent.Factory

	@Binds
	internal abstract fun bindProfileEditComponentFactory(
		impl: DefaultProfileEditComponentFactory,
	): ProfileEditComponent.Factory

	companion object {

		@Provides
		@Singleton
		fun provideProfileApi(
			@GeneralRetrofit retrofit: Retrofit,
		): ProfileApi =
			retrofit.create(ProfileApi::class.java)
	}
}
