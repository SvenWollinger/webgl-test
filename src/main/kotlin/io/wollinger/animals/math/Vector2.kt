package io.wollinger.animals.math

data class Vector2(var x: Double = 0.0, var y: Double = 0.0) {
    fun set(x: Double, y: Double) {
        this.x = x
        this.y = y
    }
    fun set(x: Int, y: Int) {
        this.x = x.toDouble()
        this.y = y.toDouble()
    }
    fun add(x: Double, y: Double) {
        this.x += x
        this.y += y
    }
    fun add(x: Int, y: Int) {
        this.x += x
        this.y += y
    }
}