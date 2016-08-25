package org.krayser.core

import org.krayser.util.Vec3D


interface GObject {
    fun intersect(ray: Ray): Vec3D?
    fun getColor(point: Vec3D): Vec3D
    fun getNormal(point: Vec3D): Vec3D
}