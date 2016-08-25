package org.krayser.core

import org.krayser.util.Vec3D


class Sphere(val center: Vec3D, val radius: Float, val color: Vec3D): GObject {

    override fun intersect(ray: Ray): Vec3D? {
        val a = ray.dir dot ray.dir
        val b = ray.dir dot ((ray.pos - center) * 2f)
        val c  = (center dot center) + (ray.pos dot ray.pos) - 2f * (ray.pos dot center) - radius * radius
        var D = b * b - 4f * a * c
        if (D < 0) {
            return null
        }
        D = Math.sqrt(D.toDouble()).toFloat()
        val t = -0.5f * (b + D) / a
        if (t > 0f) {
            return ray.pos + (ray.dir * t)
        }
        return null
    }

    override fun getColor(point: Vec3D): Vec3D {
        val l = (1f - (getNormal(point) dot point.normalized())) * 0.75f
        if (l >= 0) {
            return color * l
        } else {
            return Vec3D(0.0f)
        }
    }

    override fun getNormal(point: Vec3D) = (point - center).normalized()
}