package ru.rodipit.core.navigation

sealed interface NavigationEvent {

    data class NavigateTo(
        val screen: Screen,
    ): NavigationEvent

    data object PopBackStack: NavigationEvent

}