package io.wollinger.animals

import org.khronos.webgl.Float32Array
import org.khronos.webgl.WebGLProgram
import org.khronos.webgl.WebGLRenderingContext
import org.khronos.webgl.WebGLShader
import org.w3c.dom.Image

class Mesh(private val gl: WebGLRenderingContext) {
    val vertices: Array<Float>
    val texCoords: Array<Float>
    val colors: Array<Float>

    init {
        // Define vertex positions for each face
        vertices = arrayOf(
            // Front face
            -1.0, -1.0,  1.0,
            1.0, -1.0,  1.0,
            -1.0,  1.0,  1.0,
            1.0,  1.0,  1.0,

            // Back face
            1.0, -1.0, -1.0,
            -1.0, -1.0, -1.0,
            1.0,  1.0, -1.0,
            -1.0,  1.0, -1.0,

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
            1.0, -1.0,  1.0,
            1.0, -1.0, -1.0,
            1.0,  1.0,  1.0,
            1.0,  1.0, -1.0,

            // Left face
            -1.0, -1.0, -1.0,
            -1.0, -1.0,  1.0,
            -1.0,  1.0, -1.0,
            -1.0,  1.0,  1.0,
        ).map { it.toFloat() }.toTypedArray()

        // Define texture coordinates for each face
        texCoords = arrayOf(
            // Front face
            0.0, 0.0,
            1.0, 0.0,
            0.0, 1.0,
            1.0, 1.0,

            // Back face
            0.0, 0.0,
            1.0, 0.0,
            0.0, 1.0,
            1.0, 1.0,

            // Top face
            0.0, 0.0,
            1.0, 0.0,
            0.0, 1.0,
            1.0, 1.0,

            // Bottom face
            0.0, 0.0,
            1.0, 0.0,
            0.0, 1.0,
            1.0, 1.0,

            // Right face
            0.0, 0.0,
            1.0, 0.0,
            0.0, 1.0,
            1.0, 1.0,

            // Left face
            0.0, 0.0,
            1.0, 0.0,
            0.0, 1.0,
            1.0, 1.0,
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
        val vertexBuffer = gl.createBuffer();
        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, vertexBuffer);
        gl.bufferData(WebGLRenderingContext.ARRAY_BUFFER, Float32Array(vertices), WebGLRenderingContext.STATIC_DRAW);

        // Create buffer and bind data for texture coordinates
        val texCoordBuffer = gl.createBuffer();
        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, texCoordBuffer);
        gl.bufferData(WebGLRenderingContext.ARRAY_BUFFER, Float32Array(texCoords), WebGLRenderingContext.STATIC_DRAW);

        // Create buffer and bind data for colors
        val colorBuffer = gl.createBuffer();
        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, colorBuffer);
        gl.bufferData(WebGLRenderingContext.ARRAY_BUFFER, Float32Array(colors), WebGLRenderingContext.STATIC_DRAW);

        // Get attribute and uniform locations
        val positionAttributeLocation = gl.getAttribLocation(shaderProgram, "aPosition");
        gl.enableVertexAttribArray(positionAttributeLocation);
        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, vertexBuffer);
        gl.vertexAttribPointer(positionAttributeLocation, 3, WebGLRenderingContext.FLOAT, false, 0, 0);

        val texCoordAttributeLocation = gl.getAttribLocation(shaderProgram, "aTexCoord");
        gl.enableVertexAttribArray(texCoordAttributeLocation);
        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, texCoordBuffer);
        gl.vertexAttribPointer(texCoordAttributeLocation, 2, WebGLRenderingContext.FLOAT, false, 0, 0);

        val colorAttributeLocation = gl.getAttribLocation(shaderProgram, "aColor");
        gl.enableVertexAttribArray(colorAttributeLocation);
        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, colorBuffer);
        gl.vertexAttribPointer(colorAttributeLocation, 3, WebGLRenderingContext.FLOAT, false, 0, 0);

        repeat(6) {
            gl.drawArrays(WebGLRenderingContext.TRIANGLE_STRIP, it * 4, 4);
        }
    }

}

class Texture(path: String, private val gl: WebGLRenderingContext) {
    private val texture = gl.createTexture()
    private var ready = false

    init {
        val img = Image().apply { src = path }
        img.onload = {
            gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, texture);
            gl.texImage2D(WebGLRenderingContext.TEXTURE_2D, 0, WebGLRenderingContext.RGBA, WebGLRenderingContext.RGBA, WebGLRenderingContext.UNSIGNED_BYTE, img);
            gl.generateMipmap(WebGLRenderingContext.TEXTURE_2D);
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