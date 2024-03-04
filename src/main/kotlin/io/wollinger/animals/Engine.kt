package io.wollinger.animals

import io.wollinger.animals.math.Vector3
import io.wollinger.animals.utils.BuildInfo
import io.wollinger.animals.utils.FPSCounter
import io.wollinger.animals.utils.prettyString
import kotlinx.browser.document
import kotlinx.browser.window
import mat4
import org.khronos.webgl.Float32Array
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.js.Date
import kotlin.math.PI
import org.khronos.webgl.WebGLRenderingContext as GL

class Engine(
    private val webglCanvas: HTMLCanvasElement,
    private val hudCanvas: HTMLCanvasElement,
    private val gl: GL,
    private val hud: CanvasRenderingContext2D,
    private val shader: BaseShader,
    private val buildInfo: BuildInfo
) {
    private var showDebug = false
    private val skyColor = Color(130, 226, 255)

    private var grassRotation = 0f
    private val terrainTexture = Texture("/terrain.png", gl)

    init {
        document.onpointerup = {
            rMesh.free()
            rMesh = BlockStorageMesher.mesh(gl, BlockStorage(true))
            null
        }
    }

    private var aspect = 0f
    private val zNear = 0.1f
    private val zFar = 100f

    private val projectionMatrixLocation = shader.getUniformLocation("uProjectionMatrix")
    private val projectionMatrix = mat4.create()

    private val camPos = Vector3(0f, 0f, -30f)

    private fun update(delta: Double) {
        val speed = 0.005f
        if(Input.isPressed("w")) camPos.z += speed * delta.toFloat()
        if(Input.isPressed("s")) camPos.z += -speed * delta.toFloat()
        if(Input.isPressed("a")) camPos.x += speed * delta.toFloat()
        if(Input.isPressed("d")) camPos.x += -speed * delta.toFloat()
        if(Input.isPressed("Control")) camPos.y += speed * delta.toFloat()
        if(Input.isPressed(" ")) camPos.y += -speed * delta.toFloat()
        if(Input.isJustPressed("f")) showDebug = !showDebug



        grassRotation += 0.0005f * delta.toFloat()
    }

    private var rMesh: Mesh = BlockStorageMesher.mesh(gl, BlockStorage(true))
    private fun render() {
        if(webglCanvas.width != window.innerWidth || webglCanvas.height != window.innerHeight) {
            webglCanvas.width = window.innerWidth
            webglCanvas.height = window.innerHeight
            hudCanvas.width = window.innerWidth
            hudCanvas.height = window.innerHeight
            gl.viewport(0, 0, webglCanvas.width, webglCanvas.height)
            aspect = webglCanvas.width / webglCanvas.height.toFloat()
        }

        gl.clearColor(skyColor.r, skyColor.g, skyColor.b, 1.0f)
        gl.clear(GL.COLOR_BUFFER_BIT or GL.DEPTH_BUFFER_BIT)
        gl.enable(GL.DEPTH_TEST)
        gl.enable(GL.CULL_FACE)
        gl.cullFace(GL.BACK)
        //gl.enable(GL.BLEND)
        //gl.blendFunc(GL.SRC_ALPHA, GL.BLEND_SRC_ALPHA)

        mat4.perspective(projectionMatrix, PI / 4, aspect, zNear, zFar)

        mat4.translate(projectionMatrix, projectionMatrix, arrayOf(camPos.x, camPos.y, camPos.z))
        gl.uniformMatrix4fv(projectionMatrixLocation, false, projectionMatrix as Float32Array)

        terrainTexture.bind()

        val modelViewMatrixLocation = shader.getUniformLocation("uModelViewMatrix")
        val modelViewMatrix = mat4.create()
        mat4.rotate(modelViewMatrix, modelViewMatrix, grassRotation, arrayOf(0, 1, 0))
        gl.uniformMatrix4fv(modelViewMatrixLocation, false, modelViewMatrix as Float32Array)
        rMesh.draw(shader)

        hud.clearRect(0.0, 0.0, hudCanvas.width.toDouble(), hudCanvas.height.toDouble())
        if(showDebug) {
            var before = 0.0
            fun drawDebugText(text: String, height: Int = 50) {
                hud.fillStyle = "black"
                hud.font = "${height}px serif"
                hud.fillText(text, 0.0, before + height)
                before += height
            }
            drawDebugText("${fps.getString()} FPS")
            drawDebugText(Date(buildInfo.timestamp).prettyString(), 40)
        }
    }

    private var last = 0.0
    private val fps = FPSCounter()
    private fun loop(timestamp: Double) {
        val delta = timestamp - last
        last = timestamp

        update(delta)
        render()
        fps.frame()
        window.requestAnimationFrame(::loop)
    }

    init {
        loop(0.0)
    }
}