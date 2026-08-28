package com.nmichail.wordly.android.core.firebase.di

import com.google.firebase.auth.FirebaseAuth
import com.nmichail.wordly.android.core.firebase.data.datasource.FirebaseAuthDataSource
import com.nmichail.wordly.android.core.firebase.data.datasource.FirebaseAuthDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class FirebaseModule {

	@Binds
	@Singleton
	abstract fun bindFirebaseAuthDataSource(
		impl: FirebaseAuthDataSourceImpl,
	): FirebaseAuthDataSource

	companion object {

		@Provides
		@Singleton
		fun provideFirebaseAuth(): FirebaseAuth =
			FirebaseAuth.getInstance()
	}
}

// TODO(fcm): добавить Firebase Messaging для пушей —