package io.wollinger.animals

import org.khronos.webgl.Float32Array
import org.khronos.webgl.WebGLRenderingContext

class Mesh(
    private val gl: WebGLRenderingContext,
    private val faces: Int,
    vertices: Array<Float>,
    texCoords: Array<Float>,
    colors: Array<Float>
) {
    private val vertexBuffer = gl.createBuffer()
    private val texCoordBuffer = gl.createBuffer()
    private val colorBuffer = gl.createBuffer()

    init {
        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, vertexBuffer)
        gl.bufferData(WebGLRenderingContext.ARRAY_BUFFER, Float32Array(vertices), WebGLRenderingContext.STATIC_DRAW)
        // Create buffer and bind data for texture coordinates
        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, texCoordBuffer)
        gl.bufferData(WebGLRenderingContext.ARRAY_BUFFER, Float32Array(texCoords), WebGLRenderingContext.STATIC_DRAW)
        // Create buffer and bind data for colors
        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, colorBuffer)
        gl.bufferData(WebGLRenderingContext.ARRAY_BUFFER, Float32Array(colors), WebGLRenderingContext.STATIC_DRAW)
    }

    fun free() {
        gl.deleteBuffer(vertexBuffer)
        gl.deleteBuffer(texCoordBuffer)
        gl.deleteBuffer(colorBuffer)
    }

    fun draw(shaderProgram: BaseShader) {
        // Get attribute and uniform locations
        val positionAttributeLocation = shaderProgram.getAttribLocation("aPosition")
        gl.enableVertexAttribArray(positionAttributeLocation)
        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, vertexBuffer)
        gl.vertexAttribPointer(positionAttributeLocation, 3, WebGLRenderingContext.FLOAT, false, 0, 0)

        val texCoordAttributeLocation = shaderProgram.getAttribLocation("aTexCoord")
        gl.enableVertexAttribArray(texCoordAttributeLocation)
        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, texCoordBuffer)
        gl.vertexAttribPointer(texCoordAttributeLocation, 2, WebGLRenderingContext.FLOAT, false, 0, 0)

        val colorAttributeLocation = shaderProgram.getAttribLocation("aColor")
        gl.enableVertexAttribArray(colorAttributeLocation)
        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, colorBuffer)
        gl.vertexAttribPointer(colorAttributeLocation, 3, WebGLRenderingContext.FLOAT, false, 0, 0)

        repeat(faces) {
            gl.drawArrays(WebGLRenderingContext.TRIANGLE_STRIP, it * 4, 4)
        }
    }
}