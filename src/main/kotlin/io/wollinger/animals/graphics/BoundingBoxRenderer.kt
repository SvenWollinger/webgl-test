package io.wollinger.animals.graphics

import io.wollinger.animals.BaseShader
import io.wollinger.animals.Resources
import io.wollinger.animals.math.BoundingBox
import io.wollinger.animals.math.Matrix4
import io.wollinger.animals.math.Vector3
import io.wollinger.animals.utils.download
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.khronos.webgl.Float32Array
import org.khronos.webgl.WebGLRenderingContext

class BoundingBoxRenderer(private val gl: WebGLRenderingContext) {
    private val shader: BaseShader = BaseShader(Resources.lineVertex, Resources.lineFragment, gl)

    private val aPosition = shader.getAttribLocation("aPosition")
    private val uProjectionMatrix = shader.getUniformLocation("uProjectionMatrix")

    fun draw(boundingBox: BoundingBox, proj: Matrix4) {
        shader.use()

        val vertices = arrayOf(
            //bottom cube
            boundingBox.min.x, boundingBox.min.y, boundingBox.min.z,
            boundingBox.max.x, boundingBox.min.y, boundingBox.min.z,

            boundingBox.min.x, boundingBox.min.y, boundingBox.min.z,
            boundingBox.min.x, boundingBox.min.y, boundingBox.max.z,

            boundingBox.min.x, boundingBox.min.y, boundingBox.max.z,
            boundingBox.max.x, boundingBox.min.y, boundingBox.max.z,

            boundingBox.max.x, boundingBox.min.y, boundingBox.max.z,
            boundingBox.max.x, boundingBox.min.y, boundingBox.min.z,

            //top cube

            boundingBox.min.x, boundingBox.max.y, boundingBox.min.z,
            boundingBox.max.x, boundingBox.max.y, boundingBox.min.z,

            boundingBox.min.x, boundingBox.max.y, boundingBox.min.z,
            boundingBox.min.x, boundingBox.max.y, boundingBox.max.z,

            boundingBox.min.x, boundingBox.max.y, boundingBox.max.z,
            boundingBox.max.x, boundingBox.max.y, boundingBox.max.z,

            boundingBox.max.x, boundingBox.max.y, boundingBox.max.z,
            boundingBox.max.x, boundingBox.max.y, boundingBox.min.z,

            //Connecting y lines
            boundingBox.min.x, boundingBox.min.y, boundingBox.min.z,
            boundingBox.min.x, boundingBox.max.y, boundingBox.min.z,

            boundingBox.min.x, boundingBox.min.y, boundingBox.max.z,
            boundingBox.min.x, boundingBox.max.y, boundingBox.max.z,

            boundingBox.max.x, boundingBox.min.y, boundingBox.min.z,
            boundingBox.max.x, boundingBox.max.y, boundingBox.min.z,

            boundingBox.max.x, boundingBox.min.y, boundingBox.max.z,
            boundingBox.max.x, boundingBox.max.y, boundingBox.max.z,
        )

        val vertexBuffer = gl.createBuffer()
        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, vertexBuffer)
        gl.bufferData(WebGLRenderingContext.ARRAY_BUFFER, Float32Array(vertices), WebGLRenderingContext.STATIC_DRAW)

        gl.uniformMatrix4fv(uProjectionMatrix, false, proj.toFloat32Array())

        //Draw
        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, vertexBuffer)
        gl.vertexAttribPointer(aPosition, 3, WebGLRenderingContext.FLOAT, false, 0, 0)
        gl.enableVertexAttribArray(aPosition)
        gl.drawArrays(WebGLRenderingContext.LINES, 0, vertices.size / 3)

        //Cleanup
        gl.deleteBuffer(vertexBuffer)
    }
}