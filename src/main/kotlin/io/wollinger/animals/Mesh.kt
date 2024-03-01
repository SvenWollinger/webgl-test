package io.wollinger.animals

import org.khronos.webgl.Float32Array
import org.khronos.webgl.WebGLProgram
import org.khronos.webgl.WebGLRenderingContext
import org.khronos.webgl.WebGLShader
import org.w3c.dom.Image

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

class Mesh(private val gl: WebGLRenderingContext, block: Block) {
    private val vertices: Array<Float>
    private val texCoords: Array<Float>
    private val colors: Array<Float>

    init {
        // Define vertex positions for each face
        vertices = arrayOf(
            // Front face
            -1.0,  1.0,  1.0,
            1.0,  1.0,  1.0,
            -1.0, -1.0,  1.0,
            1.0, -1.0,  1.0,

            // Back face
            1.0,  1.0, -1.0,
            -1.0,  1.0, -1.0,
            1.0, -1.0, -1.0,
            -1.0, -1.0, -1.0,

            // Top face
            -1.0,  1.0, -1.0,
            -1.0,  1.0,  1.0,
            1.0,  1.0, -1.0,
            1.0,  1.0,  1.0,

            // Bottom face
            -1.0, -1.0,  1.0,
            -1.0, -1.0, -1.0,
            1.0, -1.0,  1.0,
            1.0, -1.0, -1.0,

            // Right face
            1.0,  1.0,  1.0,
            1.0,  1.0, -1.0,
            1.0, -1.0,  1.0,
            1.0, -1.0, -1.0,

            // Left face
            -1.0,  1.0, -1.0,
            -1.0,  1.0,  1.0,
            -1.0, -1.0, -1.0,
            -1.0, -1.0,  1.0,
        ).map { it.toFloat() }.toTypedArray()

        val front = block.front
        val back = block.back
        val top = block.top
        val bottom = block.bottom
        val left = block.left
        val right = block.right

        // Define texture coordinates for each face
        val m = 1 / 16f
        texCoords = arrayOf(
            // Front face
            front.uv[0], front.uv[1],
            front.uv[2], front.uv[3],
            front.uv[4], front.uv[5],
            front.uv[6], front.uv[7],

            // Back face
            back.uv[0], back.uv[1],
            back.uv[2], back.uv[3],
            back.uv[4], back.uv[5],
            back.uv[6], back.uv[7],

            // Top face
            top.uv[0], top.uv[1],
            top.uv[2], top.uv[3],
            top.uv[4], top.uv[5],
            top.uv[6], top.uv[7],

            // Bottom face
            bottom.uv[0], bottom.uv[1],
            bottom.uv[2], bottom.uv[3],
            bottom.uv[4], bottom.uv[5],
            bottom.uv[6], bottom.uv[7],

            // Right face
            right.uv[0], right.uv[1],
            right.uv[2], right.uv[3],
            right.uv[4], right.uv[5],
            right.uv[6], right.uv[7],

            // Left face
            left.uv[0], left.uv[1],
            left.uv[2], left.uv[3],
            left.uv[4], left.uv[5],
            left.uv[6], left.uv[7],
        ).map { it.toFloat() }.toTypedArray()

        // Define colors for each vertex (default to white)
        colors = arrayOf(
            1.0, 1.0, 1.0, // Front face
            1.0, 1.0, 1.0,
            1.0, 1.0, 1.0,
            1.0, 1.0, 1.0,

            1.0, 1.0, 1.0, // Back face
            1.0, 1.0, 1.0,
            1.0, 1.0, 1.0,
            1.0, 1.0, 1.0,

            1.0, 1.0, 1.0, // Top face
            1.0, 1.0, 1.0,
            1.0, 1.0, 1.0,
            1.0, 1.0, 1.0,

            1.0, 1.0, 1.0, // Bottom face
            1.0, 1.0, 1.0,
            1.0, 1.0, 1.0,
            1.0, 1.0, 1.0,

            1.0, 1.0, 1.0, // Right face
            1.0, 1.0, 1.0,
            1.0, 1.0, 1.0,
            1.0, 1.0, 1.0,

            1.0, 1.0, 1.0, // Left face
            1.0, 1.0, 1.0,
            1.0, 1.0, 1.0,
            1.0, 1.0, 1.0,
        ).map { it.toFloat() }.toTypedArray()

        // Create buffer and bind data for vertices

    }

    fun draw(shaderProgram: WebGLProgram) {
        val vertexBuffer = gl.createBuffer()
        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, vertexBuffer)
        gl.bufferData(WebGLRenderingContext.ARRAY_BUFFER, Float32Array(vertices), WebGLRenderingContext.STATIC_DRAW)

        // Create buffer and bind data for texture coordinates
        val texCoordBuffer = gl.createBuffer()
        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, texCoordBuffer)
        gl.bufferData(WebGLRenderingContext.ARRAY_BUFFER, Float32Array(texCoords), WebGLRenderingContext.STATIC_DRAW)

        // Create buffer and bind data for colors
        val colorBuffer = gl.createBuffer()
        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, colorBuffer)
        gl.bufferData(WebGLRenderingContext.ARRAY_BUFFER, Float32Array(colors), WebGLRenderingContext.STATIC_DRAW)

        // Get attribute and uniform locations
        val positionAttributeLocation = gl.getAttribLocation(shaderProgram, "aPosition")
        gl.enableVertexAttribArray(positionAttributeLocation)
        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, vertexBuffer)
        gl.vertexAttribPointer(positionAttributeLocation, 3, WebGLRenderingContext.FLOAT, false, 0, 0)

        val texCoordAttributeLocation = gl.getAttribLocation(shaderProgram, "aTexCoord")
        gl.enableVertexAttribArray(texCoordAttributeLocation)
        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, texCoordBuffer)
        gl.vertexAttribPointer(texCoordAttributeLocation, 2, WebGLRenderingContext.FLOAT, false, 0, 0)

        val colorAttributeLocation = gl.getAttribLocation(shaderProgram, "aColor")
        gl.enableVertexAttribArray(colorAttributeLocation)
        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, colorBuffer)
        gl.vertexAttribPointer(colorAttributeLocation, 3, WebGLRenderingContext.FLOAT, false, 0, 0)

        repeat(6) {
            gl.drawArrays(WebGLRenderingContext.TRIANGLE_STRIP, it * 4, 4)
        }
    }

}

class Texture(path: String, private val gl: WebGLRenderingContext) {
    private val texture = gl.createTexture()
    private var ready = false

    init {
        val img = Image().apply { src = path }
        img.onload = {
            gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, texture)
            gl.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_MIN_FILTER, WebGLRenderingContext.NEAREST)
            gl.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_MAG_FILTER, WebGLRenderingContext.NEAREST)
            gl.texImage2D(WebGLRenderingContext.TEXTURE_2D, 0, WebGLRenderingContext.RGBA, WebGLRenderingContext.RGBA, WebGLRenderingContext.UNSIGNED_BYTE, img)
            gl.generateMipmap(WebGLRenderingContext.TEXTURE_2D)
            ready = true
            null
        }
    }

    fun bind() {
        if(!ready) return
        gl.activeTexture(WebGLRenderingContext.TEXTURE0)
        gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, texture)
    }
}

data class UV(val x: Float, val y: Float)