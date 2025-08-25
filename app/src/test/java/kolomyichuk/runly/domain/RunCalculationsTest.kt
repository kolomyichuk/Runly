package kolomyichuk.runly.domain

import kolomyichuk.runly.domain.run.RunCalculations
import kolomyichuk.runly.domain.settings.model.DistanceUnit
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Locale

class RunCalculationsTest {

    @Test
    fun `Given meters When convertDistance to kilometers Then returns correct value`() {
        // Given
        val meters = 1500.0
        val expected = 1.5

        // When
        val result = RunCalculations.convertDistance(meters, DistanceUnit.KILOMETERS)

        // Then
        assertEquals(expected, result, 0.0001)
    }

    @Test
    fun `Given meters When convertDistance to miles Then returns correct value`() {
        // Given
        val meters = 1609.34
        val expected = 1.0

        // When
        val result = RunCalculations.convertDistance(meters, DistanceUnit.MILES)

        // Then
        assertEquals(expected, result, 0.0001)
    }

    @Test
    fun `Given valid distance and duration When calculateAvgSpeed Then returns correct speed`() {
        // Given
        val distance = 5.0
        val duration = 30 * 60 * 1000L
        val expected = String.format(Locale.US, "%.2f", 10.0)

        // When
        val result = RunCalculations.calculateAvgSpeed(distance, duration)

        // Then
        assertEquals(expected, result)
    }

    @Test
    fun `Given distance and too short duration When calculateAvgSpeed Then returns 0_00`() {
        // Given
        val distance = 5.0
        val duration = 2000L

        // When
        val result = RunCalculations.calculateAvgSpeed(distance, duration)

        // Then
        assertEquals("0.00", result)
    }

    @Test
    fun `Given too short distance and valid duration When calculateAvgSpeed Then returns 0_00`() {
        // Given
        val distance = 0.005
        val duration = 10000L

        // When
        val result = RunCalculations.calculateAvgSpeed(distance, duration)

        // Then
        assertEquals("0.00", result)
    }

    @Test
    fun `Given distance and zero duration When calculateAvgSpeed Then returns 0_00`() {
        // Given
        val distance = 5.0
        val duration = 0L

        // When
        val result = RunCalculations.calculateAvgSpeed(distance, duration)

        // Then
        assertEquals("0.00", result)
    }
}
