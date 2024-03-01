package io.wollinger.animals

enum class Block (
    val top: Atlas,
    val bottom: Atlas,
    val front: Atlas,
    val back: Atlas,
    val left: Atlas,
    val right: Atlas
){
    GRASS(
        top = Atlas.GRASS_TOP,
        bottom = Atlas.DIRT,
        front = Atlas.GRASS_SIDE,
        back = Atlas.GRASS_SIDE,
        left = Atlas.GRASS_SIDE,
        right = Atlas.GRASS_SIDE
    ),
    DIRT(
        top = Atlas.DIRT,
        bottom = Atlas.DIRT,
        front = Atlas.DIRT,
        back = Atlas.DIRT,
        left = Atlas.DIRT,
        right = Atlas.DIRT
    ),
    TNT(
        top = Atlas.TNT_TOP,
        bottom = Atlas.TNT_BOTTOM,
        front = Atlas.TNT_SIDE,
        back = Atlas.TNT_SIDE,
        left = Atlas.TNT_SIDE,
        right = Atlas.TNT_SIDE
    )
}