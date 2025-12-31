import eu.anifantakis.ksafe_demo.di.platformModule
import eu.anifantakis.ksafe_demo.di.sharedModule
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(sharedModule, platformModule)
    }
}
