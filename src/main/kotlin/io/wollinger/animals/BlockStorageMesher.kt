package io.wollinger.animals

import io.wollinger.animals.utils.addAll
import org.khronos.webgl.WebGLRenderingContext

object BlockStorageMesher {
    fun mesh(gl: WebGLRenderingContext, storage: BlockStorage): Mesh {
        // Define vertex positions for each face
        val vertices = ArrayList<Float>()
        val texCoords = ArrayList<Float>()
        val colors = ArrayList<Float>()
        val m = 1 / 16f
        var faces = 0

        fun add(
            block: Block,
            x: Int,
            y: Int,
            z: Int,
            top: Boolean,
            bottom: Boolean,
            left: Boolean,
            right: Boolean,
            front: Boolean,
            back: Boolean
        ) {
            val pX = x + 0.5f - 8f
            val mX = x + -0.5f - 8f
            val pY = y + 0.5f - 8f
            val mY = y + -0.5f - 8f
            val pZ = z + 0.5f - 8f
            val mZ = z + -0.5f - 8f

            if(front) {
                vertices.addAll(
                    mX,  pY,  pZ,
                    pX,  pY, pZ,
                    mX, mY, pZ,
                    pX, mY, pZ,
                )
                texCoords.addAll(
                    block.front.uv[0], block.front.uv[1],
                    block.front.uv[2], block.front.uv[3],
                    block.front.uv[4], block.front.uv[5],
                    block.front.uv[6], block.front.uv[7],
                )
                colors.addAll(
                    1.0f, 1.0f, 1.0f, // Front face
                    1.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f,
                )
                faces++
            }

            if(back) {
                vertices.addAll(
                    pX,  pY, mZ,
                    mX,  pY, mZ,
                    pX, mY, mZ,
                    mX, mY, mZ,
                )
                texCoords.addAll(
                    block.back.uv[0], block.back.uv[1],
                    block.back.uv[2], block.back.uv[3],
                    block.back.uv[4], block.back.uv[5],
                    block.back.uv[6], block.back.uv[7],
                )
                colors.addAll(
                    1.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f
                )
                faces++
            }

            if(top) {
                vertices.addAll(
                    mX,  pY, mZ,
                    mX,  pY,  pZ,
                    pX,  pY, mZ,
                    pX,  pY,  pZ,
                )
                texCoords.addAll(
                    block.top.uv[0], block.top.uv[1],
                    block.top.uv[2], block.top.uv[3],
                    block.top.uv[4], block.top.uv[5],
                    block.top.uv[6], block.top.uv[7]
                )
                colors.addAll(
                    1.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f,
                )
                faces++
            }

            if(bottom) {
                vertices.addAll(
                    mX, mY, pZ,
                    mX, mY, mZ,
                    pX, mY, pZ,
                    pX, mY, mZ,
                )
                texCoords.addAll(
                    block.bottom.uv[0], block.bottom.uv[1],
                    block.bottom.uv[2], block.bottom.uv[3],
                    block.bottom.uv[4], block.bottom.uv[5],
                    block.bottom.uv[6], block.bottom.uv[7]
                )
                colors.addAll(
                    1.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f
                )
                faces++
            }

            if(right) {
                vertices.addAll(
                    pX,  pY,  pZ,
                    pX,  pY, mZ,
                    pX, mY,  pZ,
                    pX, mY, mZ,
                )
                texCoords.addAll(
                    block.right.uv[0], block.right.uv[1],
                    block.right.uv[2], block.right.uv[3],
                    block.right.uv[4], block.right.uv[5],
                    block.right.uv[6], block.right.uv[7]
                )
                colors.addAll(
                    1.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f
                )
                faces++
            }

            if(left) {
                vertices.addAll(
                    mX, pY, mZ,
                    mX, pY, pZ,
                    mX, mY, mZ,
                    mX, mY, pZ,
                )
                texCoords.addAll(
                    block.left.uv[0], block.left.uv[1],
                    block.left.uv[2], block.left.uv[3],
                    block.left.uv[4], block.left.uv[5],
                    block.left.uv[6], block.left.uv[7]
                )
                colors.addAll(
                    1.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f
                )
                faces++
            }
        }

        storage.forEach { x, y, z, block ->
            fun get(x: Int, y: Int, z: Int): Boolean {
                return try {
                    storage.get(x, y, z) == null
                } catch (e: Exception) {
                    if(0 > x) println("$x $y $z")

                    true
                }
            }
            block?.let {
                add(
                    block = it,
                    x = x,
                    y = y,
                    z= z,
                    top = get(x, y + 1, z),
                    bottom = get(x, y - 1, z),
                    left = get(x - 1, y, z),
                    right = get(x + 1, y, z),
                    front = get(x, y, z + 1),
                    back = get(x, y, z - 1)
                )
            }
        }

        // Create buffer and bind data for vertices
        return Mesh(gl, faces, vertices.toTypedArray(), texCoords.toTypedArray(), colors.toTypedArray())
    }
}