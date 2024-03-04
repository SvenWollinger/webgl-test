package io.wollinger.animals

data class Color(
    var r: Float,
    var g: Float,
    var b: Float,
    var a: Float
) {
    constructor(
        r: Int,
        g: Int,
        b: Int,
        a: Int = 255
    ): this(r / 255f, g / 255f, b / 255f, a / 255f)

    fun set(
        r: Int = (this.r * 255).toInt(),
        g: Int = (this.g * 255).toInt(),
        b: Int = (this.b * 255).toInt(),
        a: Int = (this.a * 255).toInt()
    ) {
        this.r = r / 255f
        this.g = g / 255f
        this.b = b / 255f
        this.a = a / 255f
    }
}