package io.wollinger.animals.math

import kotlin.math.sqrt

data class Vector3(var x: Float = 0f, var y: Float = 0f, var z: Float) {
    constructor(x: Number = 0, y: Number= 0, z: Number= 0): this(x.toFloat(), y.toFloat(), z.toFloat())
    constructor(vector3: Vector3): this(vector3.x, vector3.y, vector3.z)

    fun times(f: Float) = set(x = x * f, y = y * f, z = z * f)

    fun add(other: Vector3) = set(x = x + other.x, y = y + other.y, z = z + other.z)

    fun add(x: Number, y: Number, z: Number) = set(this.x + x.toFloat(), this.y + y.toFloat(), this.z + z.toFloat())

    fun set(x: Float, y: Float, z: Float): Vector3 {
        this.x = x
        this.y = y
        this.z = z
        return this
    }

    fun crs(other: Vector3) = set(
        x = y * other.z - z * other.y,
        y = z * other.x - x * other.z,
        z = x * other.y - y * other.x
    )

    fun nor(): Vector3 {
        val length = sqrt(x * x + y * y + z * z)
        if (length != 0f) {
            val invLength = 1f / length
            set(x = x * invLength, y = y * invLength, z = z * invLength)
        }
        return this
    }
    fun toArray() = arrayOf(x, y, z)
    val length: Float get() = sqrt(x * x + y * y + z * z)
    fun scale(scalar: Float) = set(x = x * scalar, y = y * scalar, z = z * scalar)

    fun transformQuat(quat: Quaternion): Vector3 {
        // Quaternion multiplication: qv = q * v * q_conjugate
        // where q_conjugate is the conjugate of the quaternion q
        val (qx, qy, qz, qw) = listOf(quat.x, quat.y, quat.z, quat.w)
        val (vx, vy, vz) = listOf(x, y, z)

        // Compute intermediate quaternion qv = q * v
        val qvX = qw * vx + qy * vz - qz * vy
        val qvY = qw * vy + qz * vx - qx * vz
        val qvZ = qw * vz + qx * vy - qy * vx
        val qvW = -qx * vx - qy * vy - qz * vz

        // Compute the final quaternion qv = qv * q_conjugate
        val resultX = qvW * -qx + qvX * qw + qvY * -qz - qvZ * -qy
        val resultY = qvW * -qy + qvY * qw + qvZ * -qx - qvX * -qz
        val resultZ = qvW * -qz + qvZ * qw + qvX * -qy - qvY * -qx

        return set(x = resultX, y = resultY, z = resultZ)
    }

    fun invert() = set(x = -x, y = -y, z = -z)

    fun formatString(digits: Int = 2): String {
        val x = x.asDynamic().toFixed(digits)
        val y = y.asDynamic().toFixed(digits)
        val z = z.asDynamic().toFixed(digits)
        return "Vector3(x=$x, y=$y, z=$z)"
    }

    companion object {
        val UP = Vector3(0, 1, 0)
        val DOWN = Vector3(0, -1, 0)
    }
}