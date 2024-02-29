import kotlinx.browser.document
import kotlinx.browser.window
import org.khronos.webgl.Float32Array
import org.khronos.webgl.WebGLRenderingContext
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.Image
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener
import kotlin.math.PI

fun main() {
    window.addEventListener("load", object: EventListener {
        override fun handleEvent(event: Event) {
            init()
        }

    })

}

fun init() {
            val canvas = document.getElementById("webgl-canvas") as HTMLCanvasElement
            val gl = canvas.getContext("webgl") as WebGLRenderingContext

            if (gl == null) {
                console.error("Unable to initialize WebGL. Your browser may not support it.");
                return;
            }

            // Define vertex positions for each face
            val vertices = arrayOf(
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
            val texCoords = arrayOf(
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
           val colors = arrayOf(
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

            // Define vertex shader
            val vsSource = """
                attribute vec3 aPosition;
                attribute vec2 aTexCoord;
                attribute vec3 aColor;
                varying highp vec2 vTextureCoord;
                varying vec3 vColor;
                uniform mat4 uModelViewMatrix;
                uniform mat4 uProjectionMatrix;
    
                void main() {
                    gl_Position = uProjectionMatrix * uModelViewMatrix * vec4(aPosition, 1.0);
                    vTextureCoord = aTexCoord;
                    vColor = aColor;
                }
            """.trimIndent()

            // Define fragment shader
            val fsSource = """
                precision mediump float;
                varying highp vec2 vTextureCoord;
                varying vec3 vColor;
                uniform sampler2D uSampler;
    
                void main() {
                    gl_FragColor = texture2D(uSampler, vTextureCoord) * vec4(vColor, 1.0);
                }
            """.trimIndent()

            // Compile and link shaders
            val vertexShader = gl.createShader(WebGLRenderingContext.VERTEX_SHADER);
            gl.shaderSource(vertexShader, vsSource);
            gl.compileShader(vertexShader);

            val fragmentShader = gl.createShader(WebGLRenderingContext.FRAGMENT_SHADER);
            gl.shaderSource(fragmentShader, fsSource);
            gl.compileShader(fragmentShader);

    val shaderProgram = gl.createProgram();
            gl.attachShader(shaderProgram, vertexShader);
            gl.attachShader(shaderProgram, fragmentShader);
            gl.linkProgram(shaderProgram);
            gl.useProgram(shaderProgram);

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

            val modelViewMatrixLocation = gl.getUniformLocation(shaderProgram, "uModelViewMatrix");
            val projectionMatrixLocation = gl.getUniformLocation(shaderProgram, "uProjectionMatrix");

            // Set up projection matrix
            val projectionMatrix = mat4.create();
            mat4.perspective(projectionMatrix, PI / 4, canvas.clientWidth / canvas.clientHeight, 0.1, 100.0);
            gl.uniformMatrix4fv(projectionMatrixLocation, false, projectionMatrix as Float32Array);

            // Set up model view matrix
            val modelViewMatrix = mat4.create();
            mat4.translate(modelViewMatrix, modelViewMatrix, arrayOf(-0.0, 0.0, -6.0));
            gl.uniformMatrix4fv(modelViewMatrixLocation, false, modelViewMatrix as Float32Array);

            // Load texture
            val texture = gl.createTexture();
            val image = Image();
            image.onload = {
                gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, texture);
                gl.texImage2D(WebGLRenderingContext.TEXTURE_2D, 0, WebGLRenderingContext.RGBA, WebGLRenderingContext.RGBA, WebGLRenderingContext.UNSIGNED_BYTE, image);
                gl.generateMipmap(WebGLRenderingContext.TEXTURE_2D);
            }
            image.src = "/img/dirt.png"; // Replace 'texture.jpg' with your image file

            // Set texture uniform
            val samplerLocation = gl.getUniformLocation(shaderProgram, "uSampler");
            gl.uniform1i(samplerLocation, 0); // 0 is the texture unit index

            // Clear canvas and draw
            fun drawScene() {
                gl.clearColor(0.0f, 0.0f, 0.0f, 1.0f);
                gl.clear(WebGLRenderingContext.COLOR_BUFFER_BIT or WebGLRenderingContext.DEPTH_BUFFER_BIT);
                gl.enable(WebGLRenderingContext.DEPTH_TEST);

                // Draw each face as a triangle strip
                repeat(6) {
                    gl.drawArrays(WebGLRenderingContext.TRIANGLE_STRIP, it * 4, 4);
                }
            }

            // Animation loop
            fun animate(d: Double) {

                // Update rotation angle
                val angle = 0.005f; // rotate 15 degrees per second

                mat4.rotate(modelViewMatrix, modelViewMatrix, angle, arrayOf(1, 1, 0));

                // Set the updated model view matrix
                gl.uniformMatrix4fv(modelViewMatrixLocation, false, modelViewMatrix as Float32Array);

                // Draw the scene
                drawScene();
                window.requestAnimationFrame(::animate);
            }

            // Start the animation
            animate(0.0);
}