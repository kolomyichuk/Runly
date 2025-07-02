package kolomyichuk.runly.ui.screens.settings

import androidx.annotation.StringRes

interface SignOutEffect {
    object Success : SignOutEffect
    data class Failure(@StringRes val messageResId: Int) : SignOutEffect
}