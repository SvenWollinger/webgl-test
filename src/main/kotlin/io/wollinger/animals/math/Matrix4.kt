package io.wollinger.animals.math

import mat4
import org.khronos.webgl.Float32Array

class Matrix4 {
    private val data = mat4.create()

    fun fromQuat(quaternion: Quaternion) {
        mat4.fromQuat(data, quaternion.data)
    }

    fun perspective(fov: Float, aspect: Float, near: Float, far: Float) {
        mat4.perspective(data, fov, aspect, near, far)
    }

    fun translate(x: Double, y: Double, z: Double) {
        mat4.translate(data, data, arrayOf(x, y, z))
    }

    fun translate(position: Vector3) {
        mat4.translate(data, data, arrayOf(position.x, position.y, position.z))
    }

    fun resetTranslation() {
        mat4.fromTranslation(data, arrayOf(0, 0, 0))
    }

    fun translation(position: Vector3) {
        resetTranslation()
        mat4.translate(data, data, arrayOf(position.x, position.y, position.z))
    }

    fun lookAt(position: Vector3, target: Vector3, up: Vector3 = Vector3.UP) {
        mat4.lookAt(data, position.toArray(), target.toArray(), up.toArray())
    }

    fun multiply(a: Matrix4, b: Matrix4) {
        mat4.multiply(data, a.data, b.data)
    }

    fun toFloat32Array() = data as Float32Array

    fun getForwardVector() = Vector3(-data[8] as Float, -data[9] as Float, -data[10] as Float).nor()



    fun ortho(left: Float, right: Float, bottom: Float, top: Float, near: Float, far: Float) {
        mat4.ortho(data, left, right, bottom, top, near, far)
    }

    companion object {
        fun invert(out: Matrix4, from: Matrix4) {
            mat4.invert(out.data, from.data)
        }
    }
}