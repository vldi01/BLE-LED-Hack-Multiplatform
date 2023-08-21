package di

import architecture.ViewModel
import com.diachuk.routing.Routing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import org.koin.dsl.module

val commonDiModule = module {
    single { Routing() }
    single { CoroutineScope(Job()) }
    single { ViewModel(get(), get()) }
}