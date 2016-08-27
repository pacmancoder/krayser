package org.krayser.core

import org.krayser.util.Vec3D
import java.util.concurrent.atomic.AtomicBoolean

class Chunk(val krayser: KrayserController, val workGroup: ChunkRect): Runnable {

    private val surface: ByteArray = ByteArray(workGroup.w * workGroup.h * 3)
    private var isReady = AtomicBoolean(false)

    fun obtainSurface(): ByteArray? {
        if (isReady.get()) {
            isReady.set(false)
            return surface
        }
        return null
    }
    private fun setPixel(x: Int, y: Int, c: Vec3D) {
        if (c.x > 1) c.x = 1f
        if (c.y > 1) c.y = 1f
        if (c.z > 1) c.z = 1f
        surface[(y * workGroup.w + x) * 3] = (c.x * 255).toByte()
        surface[(y * workGroup.w + x) * 3 + 1] = (c.y * 255).toByte()
        surface[(y * workGroup.w + x) * 3 + 2] = (c.z * 255).toByte()
    }

    private fun getColor(ray: Ray, pass: Int = 16): Vec3D {
        val intersection = krayser.scene.objManager.intersect(ray)
        if (intersection != null) {
            val (obj, point) = intersection
            var color = obj.getColor(point)
            if (pass > 0) {
                val rRay = Ray(point, ray.dir.reflect(obj.getNormal(point)))
                val rColor = getColor(rRay, pass - 1)
                color += rColor * 0.75f
            }
            return color
        } else {
            return Vec3D(0f)
        }
    }
    override fun run() {
        isReady.set(false)
        for ((globalX, globalY) in workGroup) {
            val localX = globalX - workGroup.x
            val localY = globalY - workGroup.y
            val rays = arrayOf(
                    krayser.scene.cam.generateRay(globalX.toFloat() + 0.25f, globalY.toFloat() + 0.25f),
                    krayser.scene.cam.generateRay(globalX.toFloat() + 0.25f, globalY.toFloat() - 0.25f),
                    krayser.scene.cam.generateRay(globalX.toFloat() - 0.25f, globalY.toFloat() + 0.25f),
                    krayser.scene.cam.generateRay(globalX.toFloat() - 0.25f, globalY.toFloat() - 0.25f)
            )
            var color = Vec3D(0f)
            for (ray in rays) {
                color += getColor(ray, 16)
            }
            color /= rays.size.toFloat()
            setPixel(localX, localY, color)
            if (Thread.interrupted()) return
        }
        isReady.set(true)
    }
}

