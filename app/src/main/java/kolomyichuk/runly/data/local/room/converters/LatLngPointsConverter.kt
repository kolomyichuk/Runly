package kolomyichuk.runly.data.local.room.converters

import kolomyichuk.runly.data.local.room.entity.LatLngPoint

class LatLngPointsConverter {

    fun fromLatLngPointsListList(value: List<List<LatLngPoint>>): String {
        return value.joinToString(";") { list ->
            list.joinToString(",") { "${it.latitude}, ${it.longitude}" }
        }
    }

    fun toLatLngPointsListList(value: String): List<List<LatLngPoint>> {
        return value.split(";").map { part ->
            part.split(",").chunked(2) { LatLngPoint(it[0].toDouble(), it[1].toDouble()) }
        }
    }
}