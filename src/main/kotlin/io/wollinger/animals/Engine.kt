package io.wollinger.animals

import io.wollinger.animals.graphics.BoundingBoxRenderer
import io.wollinger.animals.graphics.UIButton
import io.wollinger.animals.math.*
import io.wollinger.animals.utils.BuildInfo
import io.wollinger.animals.utils.FPSCounter
import io.wollinger.animals.utils.cap
import io.wollinger.animals.utils.prettyString
import io.wollinger.animals.world.World
import kotlinx.browser.window
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.js.Date
import kotlin.math.PI
import org.khronos.webgl.WebGLRenderingContext as GL

data class Camera(
    var aspect: Float = 0f,
    var fov: Float = PI.toFloat() / 4f,
    val zNear: Float = 0.1f,
    val zFar: Float = 500f,
    val pos: Vector3 = Vector3(0f, 0f, 0f),
    val rotation: Quaternion = Quaternion()
)

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
    private val world = World()

    private val terrainTexture = Texture("/terrain.png", gl)

    private val camera = Camera(pos = Vector3(0, 10, -10)).apply {
        rotation.rotateX(0f)
    }
    private val projectionMatrixLocation = shader.getUniformLocation("uProjectionMatrix")

    private var viewMatrix = Matrix4()
    private val projectionMatrix = Matrix4()
    private val viewProjectionMatrix = Matrix4()

    private val dragSensitivity = 0.001f
    private val zoomRange = 15f..40f
    private val rotationRange = 60f

    init {
        UIManager.add(
            UIButton(
                image = Resources.button3d,
                onResize = { width, height ->
                    val size = width / 16
                    Rectangle(0, height - size, size, size)
                },
                onClick = { do3d = false },
                drawCondition = { do3d }
            ),
            UIButton(
                image = Resources.button2d,
                onResize = { width, height ->
                    val size = width / 16
                    Rectangle(0, height - size, size, size)
                },
                onClick = { do3d = true },
                drawCondition = { !do3d }
            ),
            UIButton(
                image = Resources.buttonDebug,
                onResize = { width, height ->
                    val size = width / 16
                    val margin = size / 4
                    Rectangle(size + margin, height - size, size * 2, size)
                },
                onClick = { showDebug = !showDebug }
            )
        )
    }

    var rotX = 1.0f
    private fun update(delta: Double) {
        if(Input.isJustPressed("f")) showDebug = !showDebug
        if(Input.isJustPressed("z")) do3d = !do3d

        camera.pos.y += Input.scroll.toFloat() * 0.005f
        camera.pos.y = zoomRange.cap(camera.pos.y)
        //camera.rotation.rotateX(Input.scroll.toFloat() * 0.0005f)
        if(Input.isJustPressed("o")) {
            println("Rotate")
            //camera.rotation.rotateX(0.10f)
            rotX += 0.1f
        }
        if(Input.isJustPressed("p")) {
            println("Rotate")
            //camera.rotation.rotateX(0.10f)
            rotX -= 0.1f
        }
        camera.rotation.setAxisAngle(Vector3(1, 0, 0), rotX)
        Input.scroll = 0.0
        camera.pos.add(
            x = -Input.dragDelta.x * dragSensitivity * camera.pos.y,
            y = 0,
            z = -Input.dragDelta.y * dragSensitivity * camera.pos.y
        )
        Input.dragDelta.set(0, 0)

        Input.didJustTouch?.let { UIManager.clickEvent(it) }
    }

    var do3d = true
    private val bboxRenderer = BoundingBoxRenderer(gl)
    private fun render() {
        shader.use()

        if(webglCanvas.width != window.innerWidth || webglCanvas.height != window.innerHeight) {
            webglCanvas.width = window.innerWidth
            webglCanvas.height = window.innerHeight
            hudCanvas.width = window.innerWidth
            hudCanvas.height = window.innerHeight
            gl.viewport(0, 0, webglCanvas.width, webglCanvas.height)
            camera.aspect = webglCanvas.width / webglCanvas.height.toFloat()
            UIManager.resize(webglCanvas.width, webglCanvas.height)
        }

        gl.clearColor(skyColor.r, skyColor.g, skyColor.b, 1.0f)
        gl.clear(GL.COLOR_BUFFER_BIT or GL.DEPTH_BUFFER_BIT)
        gl.enable(GL.DEPTH_TEST)
        gl.enable(GL.CULL_FACE)
        gl.cullFace(GL.BACK)
        //gl.enable(GL.BLEND)
        //gl.blendFunc(GL.SRC_ALPHA, GL.BLEND_SRC_ALPHA)

        val posPos = Vector3(camera.pos).apply { invert() }
        val rotation = Matrix4().apply { fromQuat(camera.rotation) }
        val translation = Matrix4().apply { translate(posPos) }
        viewMatrix = Matrix4()
        viewMatrix.multiply(rotation, translation)
        if(do3d) {
            projectionMatrix.perspective(camera.fov, camera.aspect, camera.zNear, camera.zFar)
        } else {
            val orthoWidth = camera.pos.y
            val orthoHeight = orthoWidth / camera.aspect

            projectionMatrix.ortho(
                -orthoWidth / 2f, orthoWidth / 2f,
                -orthoHeight / 2f, orthoHeight / 2f,
                camera.zNear, camera.zFar
            )
        }

        //Combine
        viewProjectionMatrix.multiply(projectionMatrix, viewMatrix)

        gl.uniformMatrix4fv(projectionMatrixLocation, false, viewProjectionMatrix.toFloat32Array())

        //val inverse = Matrix4()
        //Matrix4.invert(inverse, viewProjectionMatrix)
        //println(inverse.getForwardVector())


        // bind terrain.png and render world
        terrainTexture.bind()
        world.render(gl, shader, camera.pos)
        //bboxRenderer.draw(BoundingBox(Vector3(0, 0, 0), Vector3(16, 16, 16)), viewProjectionMatrix)

        // 2d ui code
        hud.clearRect(0.0, 0.0, hudCanvas.width.toDouble(), hudCanvas.height.toDouble())
        UIManager.draw(hud)

        if(showDebug) {
            var before = 0.0
            fun drawDebugText(text: String, height: Int = 50) {
                hud.fillStyle = "black"
                hud.font = "${height}px serif"
                hud.fillText(text, 0.0, before + height)
                before += height
            }
            drawDebugText("FPS: ${fps.getString()}")
            drawDebugText("Camera Pos: ${camera.pos.formatString()}")
            drawDebugText("Build Info:", 20)
            drawDebugText("Version ${buildInfo.version} rev-${buildInfo.githash}", 20)
            drawDebugText("Message: ${buildInfo.commitMessage}", 20)
            drawDebugText("Time: ${Date(buildInfo.timestamp).prettyString()}", 20)
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