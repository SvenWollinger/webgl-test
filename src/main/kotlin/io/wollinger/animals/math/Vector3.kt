package io.wollinger.animals.math

import kotlin.math.sqrt

data class Vector3(var x: Float = 0f, var y: Float = 0f, var z: Float) {
    constructor(x: Number, y: Number, z: Number): this(x.toFloat(), y.toFloat(), z.toFloat())
    fun length(): Float {
        return sqrt(x * x + y * y + z * z)
    }

    fun normalize(): Vector3 {
        val length = length()
        return Vector3(x / length, y / length, z / length)
    }
    fun toArray() = arrayOf(x, y, z)
    fun scale(scalar: Float) {
        x *= scalar
        y *= scalar
        z *= scalar
    }

    fun rotateY(angle: Float) {
        val newX = x * kotlin.math.cos(angle) - z * kotlin.math.sin(angle)
        val newZ = x * kotlin.math.sin(angle) + z * kotlin.math.cos(angle)
        x = newX
        z = newZ
    }
    fun transformQuat(quat: Quaternion) {
        // Quaternion multiplication: qv = q * v * q_conjugate
        // where q_conjugate is the conjugate of the quaternion q
        val qx = quat.data[0] as Float
        val qy = quat.data[1] as Float
        val qz = quat.data[2] as Float
        val qw = quat.data[3] as Float

        val vx = x
        val vy = y
        val vz = z

        // Compute intermediate quaternion qv = q * v
        val qvX = qw * vx + qy * vz - qz * vy
        val qvY = qw * vy + qz * vx - qx * vz
        val qvZ = qw * vz + qx * vy - qy * vx
        val qvW = -qx * vx - qy * vy - qz * vz

        // Compute the final quaternion qv = qv * q_conjugate
        val resultX = qvW * -qx + qvX * qw + qvY * -qz - qvZ * -qy
        val resultY = qvW * -qy + qvY * qw + qvZ * -qx - qvX * -qz
        val resultZ = qvW * -qz + qvZ * qw + qvX * -qy - qvY * -qx

        x = resultX
        y = resultY
        z = resultZ
    }

    companion object {
        val UP = Vector3(0f, 1f, 0f)
    }
}