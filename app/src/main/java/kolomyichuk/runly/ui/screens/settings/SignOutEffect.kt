package kolomyichuk.runly.ui.screens.settings

sealed interface SignOutEffect {
    object Success : SignOutEffect
    object Failure : SignOutEffect
}