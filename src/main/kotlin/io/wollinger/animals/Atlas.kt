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
    STONE(floatArrayOf(
        i, 0f,
        i * 2, 0f,
        i, i,
        i * 2, i
    )),
    BEDROCK(floatArrayOf(
        i, i,
        i * 2, i,
        i, i * 2,
        i * 2, i * 2
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
    GLASS_BLOCK(floatArrayOf(
        i * 1, i * 3,
        i * 2, i * 3,
        i * 1, i * 4,
        i * 2, i * 4
    )),
    COBBLESTONE(floatArrayOf(
        0f, i,
        i, i,
        0f, i * 2,
        i, i * 2
    )),
    OAK_PLANKS(floatArrayOf(
        i * 4, 0f,
        i * 5, 0f,
        i * 4, i,
        i * 5, i
    )),
    BRICKS(floatArrayOf(
        i * 7, 0f,
        i * 8, 0f,
        i * 7, i,
        i * 8, i
    )),
    SAND(floatArrayOf(
        i * 2, i,
        i * 3, i,
        i * 2, i * 2,
        i * 3, i * 2
    )),
    GRAVEL(floatArrayOf(
        i * 3, i,
        i * 4, i,
        i * 3, i * 2,
        i * 4, i * 2
    )),
    OAK_LOG_SIDE(floatArrayOf(
        i * 4, i,
        i * 5, i,
        i * 4, i * 2,
        i * 5, i * 2
    )),
    OAK_LOG_TOP(floatArrayOf(
        i * 5, i,
        i * 6, i,
        i * 5, i * 2,
        i * 6, i * 2
    )),
    IRON_BLOCK(floatArrayOf(
        i * 6, i,
        i * 7, i,
        i * 6, i * 2,
        i * 7, i * 2
    )),
    GOLD_BLOCK(floatArrayOf(
        i * 7, i,
        i * 8, i,
        i * 7, i * 2,
        i * 8, i * 2
    )),
    DIAMOND_BLOCK(floatArrayOf(
        i * 8, i,
        i * 9, i,
        i * 8, i * 2,
        i * 9, i * 2
    )),
    DIAMOND_ORE(floatArrayOf(
        i * 2, i * 3,
        i * 3, i * 3,
        i * 2, i * 4,
        i * 3, i * 4
    )),
    BOOKSHELF(floatArrayOf(
        i * 3, i * 2,
        i * 4, i * 2,
        i * 3, i * 3,
        i * 4, i * 3
    ))
}