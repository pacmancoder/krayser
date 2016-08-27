package org.krayser.core

import org.krayser.util.Vec3D

class Scene {
    // camera at 5m from center, directioned in center, with fov 90 deg
    var cam = Camera(90f, 800, 600)
    var objManager = GObjectManager()
    init {
        objManager.addObject(Sphere(Vec3D(0f, 0f, -4.5f), 2f, Vec3D(0.7f,0f,0f)))
        objManager.addObject(Sphere(Vec3D(2f, 1f, -2.5f), 1f, Vec3D(0f,1f,0f)))
        objManager.addObject(Sphere(Vec3D(-1f, -1f, -2.5f), 0.75f, Vec3D(0f,0f,1f)))
        objManager.addObject(Sphere(Vec3D(5f, -2f, -7f), 3f, Vec3D(1.0f, 1.0f, 0f)))
    }
}