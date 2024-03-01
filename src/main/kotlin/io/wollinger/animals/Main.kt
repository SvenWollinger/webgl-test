import io.wollinger.animals.Block
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

    val dirtMesh = Mesh(gl, Block.DIRT)
    val grassMesh = Mesh(gl, Block.TNT)
    var grassRotation = 0f
    val terrainTexture = Texture("/terrain.png", gl)

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

    var aspect = 0f
    val zNear = 0.1f
    val zFar = 100f

    val projectionMatrixLocation = gl.getUniformLocation(shaderProgram, "uProjectionMatrix")
    val projectionMatrix = mat4.create()

    fun drawScene() {
        if(canvas.width != window.innerWidth || canvas.height != window.innerHeight) {
            canvas.width = window.innerWidth
            canvas.height = window.innerHeight
            gl.viewport(0, 0, canvas.width, canvas.height);
            aspect = canvas.width / canvas.height.toFloat()
        }

        gl.clearColor(0.0f, 0.0f, 0.0f, 1.0f)
        gl.clear(WebGLRenderingContext.COLOR_BUFFER_BIT or WebGLRenderingContext.DEPTH_BUFFER_BIT)
        gl.enable(WebGLRenderingContext.DEPTH_TEST)

        mat4.perspective(projectionMatrix, PI / 4, aspect, zNear, zFar)
        mat4.translate(projectionMatrix, projectionMatrix, arrayOf(0.0, 0.0, -12.0))
        gl.uniformMatrix4fv(projectionMatrixLocation, false, projectionMatrix as Float32Array)

        terrainTexture.bind()
        //Draw static dirt block
        run {
            //dirtTexture.bind()
            val modelViewMatrixLocation = gl.getUniformLocation(shaderProgram, "uModelViewMatrix")
            val modelViewMatrix = mat4.create()
            mat4.translate(modelViewMatrix, modelViewMatrix, arrayOf(-0.0, 0.0, -6.0))
            gl.uniformMatrix4fv(modelViewMatrixLocation, false, modelViewMatrix as Float32Array)
            dirtMesh.draw(shaderProgram)
        }

        //Draw rotating grass block
        run {
            //grassTexture.bind()
            val modelViewMatrixLocation = gl.getUniformLocation(shaderProgram, "uModelViewMatrix")
            val modelViewMatrix = mat4.create()
            mat4.translate(modelViewMatrix, modelViewMatrix, arrayOf(-4.0, 1.0, -5.0))
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