package org.krayser.core

import org.krayser.util.Vec3D

class Camera(var fov: Float, width: Int, height: Int) {
    // will be initialized in constructor
    var rasterWidth = width.toFloat()
    var rasterHeight = height.toFloat()
    fun generateRay(x: Float, y: Float): Ray {
        val aspect = rasterWidth / rasterHeight
        val fovMul = Math.tan(Math.toRadians(fov.toDouble()) / 2.0).toFloat()
        val dir = Vec3D(
                (((x.toFloat() + 0.5f) / rasterWidth) * 2f - 1f) * aspect * fovMul,
                (1f - ((y.toFloat() + 0.5f) / rasterHeight) * 2f) * fovMul,
                -1.0f
        ).normalized()
        return Ray(Vec3D(0f), dir)
    }
}