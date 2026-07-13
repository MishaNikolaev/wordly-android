import com.nmichail.wordly.android.core.network.di.AuthModulepackage com.nmichail.wordly.android.di

import android.app.Application
import com.nmichail.wordly.android.WordlyApplication
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ComponentModule::class])
interface AppComponent {

	fun inject(application: WordlyApplication)

	@Component.Factory
	interface Factory {

		fun create(@BindsInstance application: Application): AppComponent
	}
}
