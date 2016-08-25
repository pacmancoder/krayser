package org.krayser.core

import org.krayser.util.Vec3D

class Camera(fov: Float) {
    var pos: Vec3D = Vec3D(0f)
    // will be initialized in constructor
    var fov: Float = 90f
        set(value) {
            // if we can imagine that horizontal axis of screen plane
            // have length of 2 then  we can calculate multiplier of
            // this length for getting focal length
            // so, we have triangle with one cathetus of length 1 / 2
            // and angle fov / 2, so we can get focal length
            field = value
            val focalLength = 1f / Math.tan(Math.toRadians(value / 2.0)).toFloat()
            pos = Vec3D(0f, 0f, -focalLength)
        }
    init {
        this.fov = fov
    }
}