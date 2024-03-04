package io.wollinger.animals.geo

data class GeolocationPosition(
    val coords: GeolocationCoordinates,
    val timestamp: Double
)

data class GeolocationCoordinates(
    val accuracy: Double,
    val altitude: Double?,
    val altitudeAccuracy: Double?,
    val heading: Double?,
    val latitude: Double,
    val longitude: Double,
    val speed: Double?
)