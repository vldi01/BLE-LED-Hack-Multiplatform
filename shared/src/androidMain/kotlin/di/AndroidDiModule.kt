package di

import core.Scanner
import org.koin.dsl.module

val androidDiModule = module {
    single { Scanner(get(), get()) }
}