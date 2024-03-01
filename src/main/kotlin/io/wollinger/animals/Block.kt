package io.wollinger.animals

enum class Block (
    val top: Atlas,
    val bottom: Atlas,
    val front: Atlas,
    val back: Atlas,
    val left: Atlas,
    val right: Atlas,
    val solid: Boolean = true
){
    GRASS(
        top = Atlas.GRASS_TOP,
        bottom = Atlas.DIRT,
        front = Atlas.GRASS_SIDE,
        back = Atlas.GRASS_SIDE,
        left = Atlas.GRASS_SIDE,
        right = Atlas.GRASS_SIDE
    ),
    DIRT(Atlas.DIRT),
    TNT(
        top = Atlas.TNT_TOP,
        bottom = Atlas.TNT_BOTTOM,
        front = Atlas.TNT_SIDE,
        back = Atlas.TNT_SIDE,
        left = Atlas.TNT_SIDE,
        right = Atlas.TNT_SIDE
    ),
    GLASS(all = Atlas.GLASS_BLOCK, solid = false),
    STONE(Atlas.STONE),
    BEDROCK(Atlas.BEDROCK),
    COBBLESTONE(Atlas.COBBLESTONE),
    OAK_PLANKS(Atlas.OAK_PLANKS),
    BRICKS(Atlas.BRICKS),
    SAND(Atlas.SAND),
    GRAVEL(Atlas.GRAVEL),
    OAK_LOG(
        top = Atlas.OAK_LOG_TOP,
        bottom = Atlas.OAK_LOG_TOP,
        front = Atlas.OAK_LOG_SIDE,
        back = Atlas.OAK_LOG_SIDE,
        left = Atlas.OAK_LOG_SIDE,
        right = Atlas.OAK_LOG_SIDE,
    ),
    IRON_BLOCK(Atlas.IRON_BLOCK),
    GOLD_BLOCK(Atlas.GOLD_BLOCK),
    DIAMOND_BLOCK(Atlas.DIAMOND_BLOCK),
    DIAMOND_ORE(Atlas.DIAMOND_ORE),
    ;
    constructor(all: Atlas, solid: Boolean = true) : this(all, all, all, all, all, all, solid)
}