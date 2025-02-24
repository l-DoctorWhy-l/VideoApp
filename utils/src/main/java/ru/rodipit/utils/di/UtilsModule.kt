package ru.rodipit.utils.di

import org.koin.dsl.module
import ru.rodipit.utils.StringResourceProvider

val utilsModule = module {
    single { StringResourceProvider(
        context = get()
    ) }
}