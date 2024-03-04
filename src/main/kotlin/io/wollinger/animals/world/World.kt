package io.wollinger.animals.world

import ChunkKey
import io.wollinger.animals.BaseShader
import io.wollinger.animals.Mesh
import mat4
import org.khronos.webgl.Float32Array
import org.khronos.webgl.WebGLRenderingContext

class World {
    private val chunks: HashMap<ChunkKey, Chunk> = HashMap()
    private val meshes: HashMap<ChunkKey, Mesh> = HashMap()

    init {
        fun create(x: Int, y: Int, z: Int) {
            val key = ChunkKey(x, y, z)
            val chunk = Chunk(key)
            chunks[key] = chunk
        }
        for(x in -4 until 4) {
            for(z in -4 until 4) {
                create(x, 0, z)
            }
        }

    }

    fun render(gl: WebGLRenderingContext, shader: BaseShader) {
        chunks.forEach {
            val modelViewMatrixLocation = shader.getUniformLocation("uModelViewMatrix")
            val modelViewMatrix = mat4.create()
            mat4.translate(modelViewMatrix, modelViewMatrix, arrayOf(it.key.x * Chunk.SIZE_X, it.key.y * Chunk.SIZE_Y, it.key.z * Chunk.SIZE_Z))
            gl.uniformMatrix4fv(modelViewMatrixLocation, false, modelViewMatrix as Float32Array)
            val mesh = meshes.getOrPut(it.key) { it.value.mesh(gl) }
            mesh.draw(shader)
        }
    }
}