package kolomyichuk.runly.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable
    object Home : Screen

    @Serializable
    object Profile : Screen

    @Serializable
    object Run : Screen

    @Serializable
    object Theme : Screen

    @Serializable
    object Settings : Screen

    @Serializable
    object Dashboard : Screen

    @Serializable
    data class Details(val runId: Int) : Screen
}

@Serializable
object RunScreens

@Serializable
object ProfileScreens

@Serializable
object HomeScreens

