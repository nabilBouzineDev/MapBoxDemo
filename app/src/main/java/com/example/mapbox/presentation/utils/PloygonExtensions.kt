package com.example.mapbox.presentation.utils

import com.mapbox.geojson.Point
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

private const val EARTH_RADIUS_METERS = 6_371_000.0

fun Point.nearTo(other: Point, thresholdMeters: Double = 50.0) =
    this == other || this.distanceTo(other) <= thresholdMeters

fun List<Point>.isPolygonClosed() = this.size > 2 && this.first() == this.last()

private fun Double.toRadians() = this * PI / 180.0

private fun calculateDistanceMeters(
    firstLat: Double,
    firstLon: Double,
    secondLat: Double,
    secondLon: Double
): Double {
    val deltaLat = (secondLat - firstLat).toRadians()
    val deltaLon = (secondLon - firstLon).toRadians()

    val haversine = sin(deltaLat / 2).pow(2) +
            cos(firstLat.toRadians()) * cos(secondLat.toRadians()) *
            sin(deltaLon / 2).pow(2)

    val centralAngle = 2 * atan2(sqrt(haversine), sqrt(1 - haversine))

    return EARTH_RADIUS_METERS * centralAngle
}

private fun Point.distanceTo(other: Point): Double {
    return calculateDistanceMeters(
        firstLat = this.latitude(),
        firstLon = this.longitude(),
        secondLat = other.latitude(),
        secondLon = other.longitude()
    )
}