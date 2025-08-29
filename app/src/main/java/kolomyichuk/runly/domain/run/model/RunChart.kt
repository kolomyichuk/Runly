package kolomyichuk.runly.domain.run.model

import java.util.Date

data class RunChart(
    val timestamp: Date,
    val distanceMeters: Float
)
