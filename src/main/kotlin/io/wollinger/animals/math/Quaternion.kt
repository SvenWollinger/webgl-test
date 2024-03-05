package io.wollinger.animals.math

import quat

class Quaternion {
    val data = quat.create()

    fun rotateY(rad: Float) {
        quat.rotateY(data, data, rad)
    }

    fun yaw(): Float {
        // Convert quaternion to Euler angles


        // Extract yaw angle (rotation around the vertical axis, typically the y-axis)
        // The order of angles in euler is [pitch, yaw, roll]
        return toEulerAngles().y
    }

    fun toEulerAngles(): Vector3 {
        val euler = Vector3(0, 0, 0)
        val qx = data[0] as Float
        val qy = data[1] as Float
        val qz = data[2] as Float
        val qw = data[3] as Float
        // Roll (x-axis rotation)
        val sinr_cosp = 2 * (qw * qx + qy * qz)
        val cosr_cosp = 1 - 2 * (qx * qx + qy * qy)
        euler.x = kotlin.math.atan2(sinr_cosp, cosr_cosp)

        // Pitch (y-axis rotation)
        val sinp = 2 * (qw * qy - qz * qx)
        euler.y = if (kotlin.math.abs(sinp) >= 1) {
            // Use 90 degrees if out of range
            kotlin.math.PI.toFloat() / 2 * sinp / kotlin.math.abs(sinp)
        } else {
            // Asin returns -π/2 to π/2, so we convert to -π/2 to π/2
            kotlin.math.asin(sinp)
        }

        // Yaw (z-axis rotation)
        val siny_cosp = 2 * (qw * qz + qx * qy)
        val cosy_cosp = 1 - 2 * (qy * qy + qz * qz)
        euler.z = kotlin.math.atan2(siny_cosp, cosy_cosp)

        return euler
    }
}