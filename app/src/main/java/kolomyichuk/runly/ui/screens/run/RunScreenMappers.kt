package kolomyichuk.runly.ui.screens.run

import kolomyichuk.runly.domain.run.model.RunDisplayModel

fun RunDisplayModel.toRunStartBlockState() = RunStartBlockState(
    isTracking = isTracking,
    isPause = isPause,
    distance = distance
)