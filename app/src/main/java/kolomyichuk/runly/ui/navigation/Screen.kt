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
    data class RunDetails(val runId: Int) : Screen

    @Serializable
    object UnitsOfMeasure : Screen

    @Serializable
    object SignIn : Screen

    @Serializable
    object Main : Screen
}

@Serializable
object RunGraph

@Serializable
object ProfileGraph

@Serializable
object HomeGraph

