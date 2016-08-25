package org.krayser.core

import org.krayser.util.Vec3D


class GObjectManager {
    private var objects: List<GObject> = listOf()

    fun addObject(obj: GObject) {
        objects += obj
    }

    fun intersect(ray: Ray): Pair<GObject, Vec3D>? {
        var robj: GObject? = null
        var rpoint: Vec3D? = null
        var minLen = 10000f
        for (obj in objects) {
            val point = obj.intersect(ray)
            if (point != null) {
                if ((point - ray.pos).len() < minLen) {
                    robj = obj
                    rpoint = point
                    minLen = (point - ray.pos).len()
                }
            }
        }
        if (robj == null || rpoint == null) {
            return  null
        } else {
            return Pair(robj, rpoint)
        }
    }

}