package kolomyichuk.runly.data.local.room.converters

import androidx.room.TypeConverter
import kolomyichuk.runly.data.local.room.entity.LatLngPoint
import kotlinx.serialization.json.Json

class LocationConverter {

    private val json = Json

    @TypeConverter
    fun fromLatLng(value: List<List<LatLngPoint>>): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toLatLng(value: String): List<List<LatLngPoint>> {
        return json.decodeFromString(value)
    }
}