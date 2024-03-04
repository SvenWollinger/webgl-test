package io.wollinger.animals

import io.wollinger.animals.utils.download
import kotlinx.browser.document
import org.khronos.webgl.WebGLRenderingContext
import org.w3c.dom.HTMLCanvasElement
import kotlin.js.json

suspend fun main() {
    val canvas = document.getElementById("webgl-canvas") as HTMLCanvasElement
    val gl = canvas.getContext("webgl", json(Pair("antialias", false), Pair("premultipliedAlpha", false))) as WebGLRenderingContext

    val vertexSource = download("/shaders/default.vertex.glsl").await()
    val fragmentSource = download("/shaders/default.fragment.glsl").await()
    val shader = BaseShader(vertexSource, fragmentSource, gl)

    val engine = Engine(canvas, gl, shader)
}