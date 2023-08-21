package di

import core.Scanner
import org.koin.dsl.module

val iosDiModule = module {
    single { Scanner() }
}