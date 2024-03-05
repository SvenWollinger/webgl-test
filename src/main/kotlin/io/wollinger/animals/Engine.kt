package io.wollinger.animals

import io.wollinger.animals.graphics.BoundingBoxRenderer
import io.wollinger.animals.math.BoundingBox
import io.wollinger.animals.math.Matrix4
import io.wollinger.animals.math.Quaternion
import io.wollinger.animals.math.Vector3
import io.wollinger.animals.utils.BuildInfo
import io.wollinger.animals.utils.FPSCounter
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

    private val camera = Camera(pos = Vector3(0, 0, -10))
    private val projectionMatrixLocation = shader.getUniformLocation("uProjectionMatrix")

    private var viewMatrix = Matrix4()
    private val projectionMatrix = Matrix4()
    private val viewProjectionMatrix = Matrix4()

    private fun update(delta: Double) {
        val speed = 0.005f
        if(Input.isPressed("w")) camera.pos.z += speed * delta.toFloat()
        if(Input.isPressed("s")) camera.pos.z += -speed * delta.toFloat()
        if(Input.isPressed("a")) camera.pos.x += speed * delta.toFloat()
        if(Input.isPressed("d")) camera.pos.x += -speed * delta.toFloat()
        if(Input.isPressed("c")) camera.pos.y += -speed * delta.toFloat()
        if(Input.isPressed(" ")) camera.pos.y += speed * delta.toFloat()
        if(Input.isJustPressed("f")) showDebug = !showDebug
    }

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
        }

        gl.clearColor(skyColor.r, skyColor.g, skyColor.b, 1.0f)
        gl.clear(GL.COLOR_BUFFER_BIT or GL.DEPTH_BUFFER_BIT)
        gl.enable(GL.DEPTH_TEST)
        gl.enable(GL.CULL_FACE)
        gl.cullFace(GL.BACK)
        //gl.enable(GL.BLEND)
        //gl.blendFunc(GL.SRC_ALPHA, GL.BLEND_SRC_ALPHA)


        val lookLeft = Input.isPressed("ArrowLeft")
        val lookRight = Input.isPressed("ArrowRight")

        camera.rotation.rotateY(if(lookLeft) -0.05F else if(lookRight) 0.05F else 0f)

        val dirZ = if(Input.isPressed("ArrowUp")) 1 else if(Input.isPressed("ArrowDown")) -1 else 0

        val dir = Vector3(0, 0, dirZ)
        dir.transformQuat(camera.rotation)
        dir.normalize()
        dir.scale(0.1f)

        val forward = Vector3(dir.x, dir.y, dir.z).apply { rotateY(camera.rotation.yaw()) }
        camera.pos.x += forward.x
        camera.pos.y += forward.y
        camera.pos.z += forward.z

        //camera.pos.x += dir.x
        //camera.pos.y += dir.y
        //camera.pos.z += dir.z

        val rotation = Matrix4().apply { fromQuat(camera.rotation) }
        val translation = Matrix4().apply { translate(camera.pos) }
        viewMatrix = Matrix4()
        viewMatrix.multiply(rotation, translation)


        //Set up view matrix
        //viewMatrix.lookAt(camera.pos, Vector3(0, 0, 0))

        //Set up projection matrix
        projectionMatrix.perspective(camera.fov, camera.aspect, camera.zNear, camera.zFar)

        //Combine
        viewProjectionMatrix.multiply(projectionMatrix, viewMatrix)

        gl.uniformMatrix4fv(projectionMatrixLocation, false, viewProjectionMatrix.toFloat32Array())

        //val inverse = Matrix4()
        //Matrix4.invert(inverse, viewProjectionMatrix)
        //println(inverse.getForwardVector())


        // bind terrain.png and render world
        terrainTexture.bind()
        world.render(gl, shader)
        bboxRenderer.draw(BoundingBox(Vector3(0, 0, 0), Vector3(16, 16, 16)), viewProjectionMatrix)

        // 2d ui code
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
            drawDebugText(camera.pos.toString())
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