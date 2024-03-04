package io.wollinger.animals

import io.wollinger.animals.geo.GeoUtils
import io.wollinger.animals.utils.BuildInfo
import io.wollinger.animals.utils.dl
import io.wollinger.animals.utils.download
import kotlinx.browser.document
import kotlinx.browser.window
import org.khronos.webgl.WebGLRenderingContext
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.js.json

suspend fun main() {
    val webglCanvas = document.getElementById("webgl") as HTMLCanvasElement
    val hudCanvas = document.getElementById("hud") as HTMLCanvasElement

    val gl = webglCanvas.getContext("webgl", json(Pair("antialias", false), Pair("premultipliedAlpha", false))) as WebGLRenderingContext
    val hud = hudCanvas.getContext("2d") as CanvasRenderingContext2D

    val vertexSource = download("/shaders/default.vertex.glsl").await()
    val fragmentSource = download("/shaders/default.fragment.glsl").await()
    val shader = BaseShader(vertexSource, fragmentSource, gl)

    val buildInfo = dl<BuildInfo>("/build.json").await()
    val test = GeoUtils.get().await()
    println(test)

    Engine(
        webglCanvas = webglCanvas,
        hudCanvas = hudCanvas,
        gl = gl,
        hud = hud,
        shader = shader,
        buildInfo = buildInfo
    )
}