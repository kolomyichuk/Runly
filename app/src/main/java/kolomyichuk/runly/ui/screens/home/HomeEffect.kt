package kolomyichuk.runly.ui.screens.home

import kolomyichuk.runly.data.local.room.entity.Run

sealed interface HomeEffect {
    data class ShowDeleteSnackBar(val run:Run) : HomeEffect
}