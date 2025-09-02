package kolomyichuk.runly.domain.run.usecase

import kolomyichuk.runly.domain.run.RunCalculations
import kolomyichuk.runly.domain.run.repository.RunRemoteRepository
import kolomyichuk.runly.domain.settings.model.DistanceUnit
import kolomyichuk.runly.domain.settings.repository.SettingsRepository
import kotlinx.coroutines.flow.firstOrNull
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.temporal.WeekFields
import java.util.Date

class GetThisWeekDistanceByDayUseCase(
    private val runRemoteRepository: RunRemoteRepository,
    private val settingsRepository: SettingsRepository
) {
    suspend fun invoke(): List<Float> {
        val unit = settingsRepository.getDistanceUnit().firstOrNull() ?: DistanceUnit.KILOMETERS
        val (startOfWeek, endOfWeek) = getStartAndEndOfCurrentWeek()

        val runs = runRemoteRepository.getThisWeekDistanceByDay(startOfWeek, endOfWeek)

        val groupedByDay =
            runs.groupBy { Date(it.timestamp).toLocaleDate().dayOfWeek.value }

        return (1..DAYS_IN_WEEK).map { day ->
            val dayRuns = groupedByDay[day] ?: emptyList()
            val totalMeters = dayRuns.sumOf { it.distanceMeters.toDouble() }
            val converted = RunCalculations.convertDistance(totalMeters, unit)
            converted.toFloat()
        }
    }

    private fun getStartAndEndOfCurrentWeek(): Pair<Instant, Instant> {
        val today = LocalDate.now()
        val weekFields = WeekFields.of(DayOfWeek.MONDAY, 1)

        val startOfWeek = today.with(weekFields.dayOfWeek(), 1)
        val endOfWeek = today.with(weekFields.dayOfWeek(), SUNDAY_ISO)

        val zoneId = ZoneId.systemDefault()
        val startInstant = startOfWeek.atStartOfDay(zoneId).toInstant()
        val endInstant = endOfWeek.atTime(LocalTime.MAX).atZone(zoneId).toInstant()

        return startInstant to endInstant
    }

    private fun Date.toLocaleDate(): LocalDate {
        return Instant.ofEpochMilli(this.time)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

    companion object {
        private const val SUNDAY_ISO: Long = 7
        private const val DAYS_IN_WEEK = 7
    }
}
