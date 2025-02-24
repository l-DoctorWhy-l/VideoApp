package ru.rodipit.main_screen.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.rodipit.main_screen.viemodel.MainScreenViewModel

val mainScreenModule = module {

    viewModel {
        MainScreenViewModel(
            repository = get(),
            stringResourceProvider = get(),
        )
    }

}