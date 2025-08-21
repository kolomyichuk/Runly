package kolomyichuk.runly.data.local.room.converters

import androidx.room.TypeConverter
import kolomyichuk.runly.data.local.room.entity.LatLngPoint
import kotlinx.serialization.json.Json

class LocationConverter {

    @TypeConverter
    fun fromLatLng(value: List<List<LatLngPoint>>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toLatLng(value: String): List<List<LatLngPoint>> {
        return Json.decodeFromString(value)
    }
}
