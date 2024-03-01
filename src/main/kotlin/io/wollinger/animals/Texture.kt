package io.wollinger.animals

import org.khronos.webgl.WebGLRenderingContext
import org.w3c.dom.Image

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