import io.wollinger.animals.*
import io.wollinger.animals.utils.FPSCounter
import io.wollinger.animals.utils.download
import kotlinx.browser.document
import kotlinx.browser.window
import org.khronos.webgl.Float32Array
import org.khronos.webgl.WebGLRenderingContext
import org.w3c.dom.HTMLCanvasElement
import kotlin.js.json
import kotlin.math.PI

suspend fun main() {
    init()
}

suspend fun init() {
    val canvas = document.getElementById("webgl-canvas") as HTMLCanvasElement
    val gl = canvas.getContext("webgl", json(Pair("antialias", false), Pair("premultipliedAlpha", false))) as WebGLRenderingContext

    val blockStorage = BlockStorage().apply {
        for(x in 0 until 16) {
            for(z in 0 until 16) {
                set(x, 0, z, Block.DIRT)
                set(x, 1, z, Block.DIRT)
                set(x, 2, z, Block.DIRT)
                set(x, 3, z, Block.GRASS)
            }
        }
        set(8, 4, 8, Block.TNT)
        set(8, 5, 8, Block.TNT)
        set(9, 5, 8, Block.TNT)
        set(4, 4, 8, Block.GLASS)
    }
    val blockStorageMesh = BlockStorageMesher.mesh(gl, blockStorage)
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

    var locked = false
    canvas.onclick = {
        //canvas.asDynamic().requestPointerLock()
        //locked = true
        null
    }
    var rotX = 0f
    var rotY = 0f

    document.onmousemove = {
        if(locked) {
            rotX += (it.asDynamic().movementX as Int) * 0.005f
            rotY += (it.asDynamic().movementY as Int) * 0.005f
        }
    }

    var x: Float = 0f
    var y: Float = 0f
    var z: Float = -30f
    var rMesh: Mesh = BlockStorageMesher.mesh(gl, BlockStorage(true))
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
        gl.enable(WebGLRenderingContext.BLEND)
        gl.blendFunc(WebGLRenderingContext.SRC_ALPHA, WebGLRenderingContext.BLEND_SRC_ALPHA)

        mat4.perspective(projectionMatrix, PI / 4, aspect, zNear, zFar)
        mat4.rotate(projectionMatrix, projectionMatrix, rotX, arrayOf(0, 1, 0))
        mat4.rotate(projectionMatrix, projectionMatrix, rotY, arrayOf(1, 0, 0))

        mat4.translate(projectionMatrix, projectionMatrix, arrayOf(x, y, z))
        gl.uniformMatrix4fv(projectionMatrixLocation, false, projectionMatrix as Float32Array)

        terrainTexture.bind()

        val modelViewMatrixLocation = gl.getUniformLocation(shaderProgram, "uModelViewMatrix")
        var modelViewMatrix = mat4.create()
        //mat4.translate(modelViewMatrix, modelViewMatrix, arrayOf(-4.0, 1.0, -5.0))
        mat4.rotate(modelViewMatrix, modelViewMatrix, grassRotation, arrayOf(0, 1, 0))
        gl.uniformMatrix4fv(modelViewMatrixLocation, false, modelViewMatrix as Float32Array)
        //blockStorageMesh.draw(shaderProgram)
        rMesh.draw(shaderProgram)

        //modelViewMatrix = mat4.create()
        //mat4.translate(modelViewMatrix, modelViewMatrix, arrayOf(-4.0, 1.0, -5.0))
        //gl.uniformMatrix4fv(modelViewMatrixLocation, false, modelViewMatrix as Float32Array)
        //blockStorageMesh.draw(shaderProgram)
    }


    var keys = ArrayList<String>()

    window.onkeydown = {
        if(!keys.contains(it.key)) keys.add(it.key)
    }
    window.onkeyup = { keys.remove(it.key) }

    document.onpointerup = {
        rMesh = BlockStorageMesher.mesh(gl, BlockStorage(true))
        null
    }

    var placed = false
    fun update(delta: Double) {
        if(keys.contains("r") && !placed) {
            placed = true
            rMesh = BlockStorageMesher.mesh(gl, BlockStorage(true))
        } else if(!keys.contains("r")) placed = false
        val speed = 0.005f
        if(keys.contains("w")) z += speed * delta.toFloat()
        if(keys.contains("s")) z += -speed * delta.toFloat()
        if(keys.contains("a")) x += speed * delta.toFloat()
        if(keys.contains("d")) x += -speed * delta.toFloat()
        if(keys.contains("Control")) y += speed * delta.toFloat()
        if(keys.contains(" ")) y += -speed * delta.toFloat()


        grassRotation += 0.0005f * delta.toFloat()
    }


    val fps = FPSCounter()
    var last = 0.0
    fun loop(timestamp: Double) {
        val delta = timestamp - last
        last = timestamp

        update(delta)
        drawScene()
        fps.frame()
        window.requestAnimationFrame(::loop)
    }

    loop(0.0)
}