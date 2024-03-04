package io.wollinger.animals.math

data class Vector3(var x: Float, var y: Float, var z: Float) {
    constructor(x: Number, y: Number, z: Number): this(x.toFloat(), y.toFloat(), z.toFloat())
    fun toArray() = arrayOf(x, y, z)
    companion object {
        val UP = Vector3(0f, 1f, 0f)
    }
}