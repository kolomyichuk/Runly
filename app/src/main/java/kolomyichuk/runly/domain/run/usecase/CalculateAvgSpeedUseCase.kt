package kolomyichuk.runly.domain.run.usecase

import java.util.Locale
import javax.inject.Inject

class CalculateAvgSpeedUseCase @Inject constructor() {
    operator fun invoke(distance: Double, durationInMillis: Long): String {
        val timeInSeconds = durationInMillis / 1000
        return if (timeInSeconds > 5 && distance > 0.01) {
            val speed = distance / (timeInSeconds / 3600.0)
            if (speed.isFinite()) {
                String.format(Locale.US, "%.2f", speed)
            } else "0.00"
        } else "0.00"
    }
}