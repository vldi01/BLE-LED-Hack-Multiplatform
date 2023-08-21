package di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

fun initKoin(application: Application) {
    startKoin {
        androidLogger()
        androidContext(application)
        modules(commonDiModule, androidDiModule)
    }
}