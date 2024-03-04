package io.wollinger.animals.world

import ChunkKey
import io.wollinger.animals.Block
import io.wollinger.animals.BlockStorage
import io.wollinger.animals.BlockStorageMesher
import org.khronos.webgl.WebGLRenderingContext

class Chunk(val x: Int, val y: Int, val z: Int) {
    constructor(key: ChunkKey): this(key.x, key.y, key.z)
    private val data = BlockStorage(randomBlocks = false)

    init {
        for(x in 0 until SIZE_X) {
            for(z in 0 until SIZE_Z) {
                data.set(x, 0, z, Block.DIRT)
                data.set(x, 1, z, Block.DIRT)
                data.set(x, 2, z, Block.GRASS)
            }
        }
        data.set(4, 3, 6, Block.OAK_LOG)
        data.set(4, 4, 6, Block.OAK_LOG)
        data.set(4, 5, 6, Block.OAK_LOG)

    }

    fun mesh(gl: WebGLRenderingContext) = BlockStorageMesher.mesh(gl, data)

    companion object {
        val SIZE_X = 16
        val SIZE_Y = 16
        val SIZE_Z = 16
    }
}