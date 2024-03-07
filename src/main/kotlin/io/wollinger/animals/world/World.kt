package io.wollinger.animals.world

import ChunkKey
import io.wollinger.animals.BaseShader
import io.wollinger.animals.Mesh
import io.wollinger.animals.math.Vector3
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

    private val tmp1 = Vector3()
    private val tmp2 = Vector3()
    fun render(gl: WebGLRenderingContext, shader: BaseShader, camPos: Vector3) {
        tmp1.set(camPos.x, 0f, camPos.z)
        chunks.forEach {
            tmp2.set(it.key.worldPos.x, 0f, it.key.worldPos.z)
            if(tmp1.dst(tmp2) >= 50) return@forEach
            val modelViewMatrixLocation = shader.getUniformLocation("uModelViewMatrix")
            val modelViewMatrix = mat4.create()
            mat4.translate(modelViewMatrix, modelViewMatrix, arrayOf(it.key.x * Chunk.SIZE_X, it.key.y * Chunk.SIZE_Y, it.key.z * Chunk.SIZE_Z))
            gl.uniformMatrix4fv(modelViewMatrixLocation, false, modelViewMatrix as Float32Array)
            val mesh = meshes.getOrPut(it.key) { it.value.mesh(gl) }
            mesh.draw(shader)
        }
    }
}