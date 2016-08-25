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
    override fun run() {
        isReady.set(false)
        for ((globalX, globalY) in workGroup) {
            val localX = globalX - workGroup.x
            val localY = globalY - workGroup.y
            val aspect = krayser.width.toFloat() / krayser.height.toFloat()
            val dir = (Vec3D(
                    (globalX / krayser.width.toFloat()) * 2 - 1,
                    (globalY / krayser.height.toFloat()) * 2 / aspect - 1 / aspect,
                    0f) - krayser.scene.cam.pos).normalized()
            val intersection = krayser.scene.objManager.intersect(Ray(krayser.scene.cam.pos, dir))
            if (intersection != null) {
                val (obj, point) = intersection
                setPixel(localX, localY, obj.getColor(point))
            } else {
                setPixel(localX, localY, Vec3D(0f))
            }
            if (Thread.interrupted()) return
        }
        isReady.set(true)
    }
}

