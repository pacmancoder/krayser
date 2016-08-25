package org.krayser.core

import org.krayser.util.Vec3D

class Scene {
    // camera at 5m from center, directioned in center, with fov 90 deg
    var cam = Camera(90f)
    var objManager = GObjectManager()
    init {
        objManager.addObject(Sphere(Vec3D(0.5f, -0.25f, 3f), 1f, Vec3D(1f,0f,0f)))
    }
}