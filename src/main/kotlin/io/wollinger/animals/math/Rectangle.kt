package io.wollinger.animals.math

data class Rectangle(
    var x: Double,
    var y: Double,
    var width: Double,
    var height: Double
) {
    constructor(x: Int, y: Int, width: Int, height: Int): this(x.toDouble(), y.toDouble(), width.toDouble(), height.toDouble())
    fun contains(point: Vector2): Boolean {
        val x2 = x + width
        val y2 = y + height
        return point.x >= x && point.y >= y && point.x <= x2 && point.y <= y2
    }

    fun set(rectangle: Rectangle) {
        x = rectangle.x
        y = rectangle.y
        width = rectangle.width
        height = rectangle.height
    }
}
