package kolomyichuk.runly.data.local.room.converters

import androidx.room.TypeConverter
import kolomyichuk.runly.data.local.room.entity.LatLngPoint

class LatLngPointsConverter {

    @TypeConverter
    fun fromLatLngPointsListList(value: List<List<LatLngPoint>>): String {
        return value.joinToString(";") { list ->
            list.joinToString(",") { "${it.latitude}, ${it.longitude}" }
        }
    }

    @TypeConverter
    fun toLatLngPointsListList(value: String): List<List<LatLngPoint>> {
        return value.split(";").map { part ->
            part.split(",").chunked(2) { LatLngPoint(it[0].toDouble(), it[1].toDouble()) }
        }
    }
}