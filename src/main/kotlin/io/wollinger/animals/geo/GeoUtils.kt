package io.wollinger.animals.geo

import kotlinx.browser.window
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.asDeferred
import kotlin.js.Promise

object GeoUtils {
    fun get(): Deferred<GeolocationPosition> {
        return Promise { onSuccess, _ ->
            fun parse(data: dynamic) {
                val parsed = GeolocationPosition(
                    coords = GeolocationCoordinates(
                        accuracy = data.coords.accuracy as Double,
                        altitude = data.coords.altitude as Double?,
                        altitudeAccuracy = data.coords.altitudeAccuracy as Double?,
                        heading = data.coords.heading as Double?,
                        latitude = data.coords.latitude as Double,
                        longitude = data.coords.longitude as Double,
                        speed = data.coords.speed as Double?
                    ),
                    timestamp = data.timestamp as Double
                )
                onSuccess.invoke(parsed)
            }
            window.navigator.asDynamic().geolocation.getCurrentPosition(::parse)
            Unit
        }.asDeferred()
    }
}