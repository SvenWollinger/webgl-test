package io.wollinger.animals

import io.wollinger.animals.utils.download
import org.khronos.webgl.WebGLProgram
import org.khronos.webgl.WebGLRenderingContext

class BaseShader(vertex: String, fragment: String, private val gl: WebGLRenderingContext) {
    private val shaderProgram: WebGLProgram
    init {
        // Compile and link shaders
        val vertexShader = gl.createShader(WebGLRenderingContext.VERTEX_SHADER)
        gl.shaderSource(vertexShader, vertex)
        gl.compileShader(vertexShader)

        val fragmentShader = gl.createShader(WebGLRenderingContext.FRAGMENT_SHADER)
        gl.shaderSource(fragmentShader, fragment)
        gl.compileShader(fragmentShader)

        shaderProgram = gl.createProgram() ?: throw Exception("No shader :(")
        gl.attachShader(shaderProgram, vertexShader)
        gl.attachShader(shaderProgram, fragmentShader)
        gl.linkProgram(shaderProgram)
        gl.useProgram(shaderProgram)

        // Set texture uniform
        val samplerLocation = gl.getUniformLocation(shaderProgram, "uSampler")
        gl.uniform1i(samplerLocation, 0)
    }

    fun getAttribLocation(loc: String): Int = gl.getAttribLocation(shaderProgram, loc)
    fun getUniformLocation(loc: String) = gl.getUniformLocation(shaderProgram, loc)
}