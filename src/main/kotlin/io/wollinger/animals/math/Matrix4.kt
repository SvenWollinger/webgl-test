package io.wollinger.animals.math

import mat4
import org.khronos.webgl.Float32Array

class Matrix4 {
    private val data = mat4.create()

    fun perspective(fov: Float, aspect: Float, near: Float, far: Float) {
        mat4.perspective(data, fov, aspect, near, far)
    }

    fun lookAt(position: Vector3, target: Vector3, up: Vector3 = Vector3.UP) {
        mat4.lookAt(data, position.toArray(), target.toArray(), up.toArray())
    }

    fun multiply(a: Matrix4, b: Matrix4) {
        mat4.multiply(data, a.data, b.data)
    }

    fun toFloat32Array() = data as Float32Array
}