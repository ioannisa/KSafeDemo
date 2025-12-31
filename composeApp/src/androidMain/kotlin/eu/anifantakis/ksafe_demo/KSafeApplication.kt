package eu.anifantakis.ksafe_demo

import android.app.Application
import eu.anifantakis.ksafe_demo.di.platformModule
import eu.anifantakis.ksafe_demo.di.sharedModule
import eu.anifantakis.lib.ksafe.KSafe
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class KSafeApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@KSafeApplication)
            modules(sharedModule, platformModule)
        }

        // Force initialization of KSafe to ensure ActivityLifecycleCallbacks are registered
        // before any Activity is created.
        get<KSafe>()
    }
}