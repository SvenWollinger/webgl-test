import io.wollinger.animals.Mesh
import io.wollinger.animals.Texture
import io.wollinger.animals.utils.download
import kotlinx.browser.document
import kotlinx.browser.window
import org.khronos.webgl.Float32Array
import org.khronos.webgl.WebGLRenderingContext
import org.w3c.dom.HTMLCanvasElement
import kotlin.math.PI

suspend fun main() {
    init()
}

suspend fun init() {
    val canvas = document.getElementById("webgl-canvas") as HTMLCanvasElement
    val gl = canvas.getContext("webgl") as WebGLRenderingContext

    val dirtMesh = Mesh(gl)
    val grassMesh = Mesh(gl)
    var grassRotation = 0f
    val dirtTexture = Texture("/img/dirt.png", gl)
    val grassTexture = Texture("/img/grass.png", gl)

    val vertexSource = download("/shaders/default.vertex.glsl").await()
    val fragmentSource = download("/shaders/default.fragment.glsl").await()

    // Compile and link shaders
    val vertexShader = gl.createShader(WebGLRenderingContext.VERTEX_SHADER)
    gl.shaderSource(vertexShader, vertexSource)
    gl.compileShader(vertexShader)

    val fragmentShader = gl.createShader(WebGLRenderingContext.FRAGMENT_SHADER)
    gl.shaderSource(fragmentShader, fragmentSource)
    gl.compileShader(fragmentShader)

    val shaderProgram = gl.createProgram() ?: throw Exception("No shader :(")
    gl.attachShader(shaderProgram, vertexShader)
    gl.attachShader(shaderProgram, fragmentShader)
    gl.linkProgram(shaderProgram)
    gl.useProgram(shaderProgram)

    // Set texture uniform
    val samplerLocation = gl.getUniformLocation(shaderProgram, "uSampler")
    gl.uniform1i(samplerLocation, 0)

    fun drawScene() {
        gl.clearColor(0.0f, 0.0f, 0.0f, 1.0f)
        gl.clear(WebGLRenderingContext.COLOR_BUFFER_BIT or WebGLRenderingContext.DEPTH_BUFFER_BIT)
        gl.enable(WebGLRenderingContext.DEPTH_TEST)

        // Set up projection matrix (Camera)
        val projectionMatrixLocation = gl.getUniformLocation(shaderProgram, "uProjectionMatrix")
        val projectionMatrix = mat4.create()
        mat4.perspective(projectionMatrix, PI / 4, canvas.clientWidth / canvas.clientHeight, 0.1, 100.0)
        gl.uniformMatrix4fv(projectionMatrixLocation, false, projectionMatrix as Float32Array)

        //Draw static dirt block
        run {
            dirtTexture.bind()
            val modelViewMatrixLocation = gl.getUniformLocation(shaderProgram, "uModelViewMatrix")
            val modelViewMatrix = mat4.create()
            mat4.translate(modelViewMatrix, modelViewMatrix, arrayOf(-0.0, 0.0, -6.0))
            gl.uniformMatrix4fv(modelViewMatrixLocation, false, modelViewMatrix as Float32Array)
            dirtMesh.draw(shaderProgram)
        }

        //Draw rotating grass block
        run {
            grassTexture.bind()
            val modelViewMatrixLocation = gl.getUniformLocation(shaderProgram, "uModelViewMatrix")
            val modelViewMatrix = mat4.create()
            mat4.translate(modelViewMatrix, modelViewMatrix, arrayOf(-1.0, 1.0, -6.0))
            mat4.rotate(modelViewMatrix, modelViewMatrix, grassRotation, arrayOf(1, 1, 0))
            gl.uniformMatrix4fv(modelViewMatrixLocation, false, modelViewMatrix as Float32Array)
            grassMesh.draw(shaderProgram)
        }
    }

    fun update(delta: Double) {
        grassRotation += 0.0005f * delta.toFloat()
    }

    var last = 0.0
    fun loop(timestamp: Double) {
        val delta = timestamp - last
        last = timestamp

        update(delta)
        drawScene()
        window.requestAnimationFrame(::loop)
    }

    loop(0.0)
}