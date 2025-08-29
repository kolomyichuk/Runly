package kolomyichuk.runly.domain.run.usecase

import kolomyichuk.runly.domain.run.RunCalculations
import kolomyichuk.runly.domain.run.repository.RemoteRunRepository
import kolomyichuk.runly.domain.settings.repository.SettingsRepository
import kotlinx.coroutines.flow.first
import java.util.Calendar
import java.util.Date

class GetThisWeekDistanceByDayUseCase(
    private val remoteRunRepository: RemoteRunRepository,
    private val settingsRepository: SettingsRepository
) {
    suspend fun invoke(): List<Float> {
        val unit = settingsRepository.getDistanceUnit().first()
        val (startOfWeek, endOfWeek) = getStartAndEndOfCurrentWeek()
        val runs = remoteRunRepository.getThisWeekDistanceByDay(startOfWeek, endOfWeek)

        val groupedByDay = runs.groupBy { it.timestamp.toIsoDayOfWeek() }

        return (1.rangeTo(other = 7)).map { day ->
            val dayRuns = groupedByDay[day] ?: emptyList()
            val totalMeters = dayRuns.sumOf { it.distanceMeters.toDouble() }
            val converted = RunCalculations.convertDistance(totalMeters, unit)
            converted.toFloat()
        }
    }

    private fun getStartAndEndOfCurrentWeek(): Pair<Date, Date> {
        val calendar = Calendar.getInstance().apply {
            firstDayOfWeek = Calendar.MONDAY
            set(Calendar.HOUR_OF_DAY, HOUR_START)
            set(Calendar.MINUTE, MIN_START)
            set(Calendar.SECOND, SEC_START)
            set(Calendar.MILLISECOND, MILLISECOND_START)
        }

        val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val daysFromMonday =
            if (currentDayOfWeek == Calendar.SUNDAY) {
                SUNDAY_OFFSET
            } else {
                currentDayOfWeek - Calendar.MONDAY
            }

        calendar.add(Calendar.DAY_OF_YEAR, -daysFromMonday)
        val start = calendar.time

        calendar.add(Calendar.DAY_OF_YEAR, DAYS_IN_WEEK - 1)
        calendar.set(Calendar.HOUR_OF_DAY, HOUR_END)
        calendar.set(Calendar.MINUTE, MIN_END)
        calendar.set(Calendar.SECOND, SEC_END)
        calendar.set(Calendar.MILLISECOND, MILLISECOND_MAX)
        val end = calendar.time

        return start to end
    }

    private fun Date.toIsoDayOfWeek(): Int {
        val cal = Calendar.getInstance().apply {
            time = this@toIsoDayOfWeek
            firstDayOfWeek = Calendar.MONDAY
        }
        return when (cal.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> MONDAY_ISO
            Calendar.TUESDAY -> TUESDAY_ISO
            Calendar.WEDNESDAY -> WEDNESDAY_ISO
            Calendar.THURSDAY -> THURSDAY_ISO
            Calendar.FRIDAY -> FRIDAY_ISO
            Calendar.SATURDAY -> SATURDAY_ISO
            Calendar.SUNDAY -> SUNDAY_ISO
            else -> 1
        }
    }

    companion object {
        private const val DAYS_IN_WEEK = 7
        private const val MONDAY_ISO = 1
        private const val TUESDAY_ISO = 2
        private const val WEDNESDAY_ISO = 3
        private const val THURSDAY_ISO = 4
        private const val FRIDAY_ISO = 5
        private const val SATURDAY_ISO = 6
        private const val SUNDAY_ISO = 7
        private const val MILLISECOND_MAX = 999
        private const val HOUR_START = 0
        private const val MIN_START = 0
        private const val SEC_START = 0
        private const val MILLISECOND_START = 0
        private const val HOUR_END = 23
        private const val MIN_END = 59
        private const val SEC_END = 59
        private const val SUNDAY_OFFSET = 6
    }
}
