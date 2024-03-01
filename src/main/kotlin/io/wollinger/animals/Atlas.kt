package io.wollinger.animals

private const val i = 1 / 16f

enum class Atlas(
    val uv: FloatArray
) {
    GRASS_TOP(floatArrayOf(
        0.0f, 0.0f,
        i, 0.0f,
        0.0f, i,
        i, i
    )),
    GRASS_SIDE(floatArrayOf(
        i * 3, 0f,
        i * 4, 0f,
        i * 3, i,
        i * 4, i
    )),
    DIRT(floatArrayOf(
        i * 2, 0f,
        i * 3, 0f,
        i * 2, i,
        i * 3, i
    )),

    TNT_SIDE(floatArrayOf(
        i * 8, 0f,
        i * 9, 0f,
        i * 8, i,
        i * 9, i
    )),
    TNT_TOP(floatArrayOf(
        i * 9, 0f,
        i * 10, 0f,
        i * 9, i,
        i * 10, i
    )),
    TNT_BOTTOM(floatArrayOf(
        i * 10, 0f,
        i * 11, 0f,
        i * 10, i,
        i * 11, i
    )),
}