package org.krayser.core

import java.util.concurrent.atomic.AtomicBoolean

class Chunk(val scane: Scene, val workGroup: ChunkRect): Runnable {

    private val surface: ByteArray = ByteArray(workGroup.w * workGroup.h * 3)
    private var isReady = AtomicBoolean(false)

    fun obtainSurface(): ByteArray? {
        if (isReady.get()) {
            isReady.set(false)
            return surface
        }
        return null
    }

    override fun run() {
        isReady.set(false)
        for ((globalX, globalY) in workGroup) {
            val x = globalX - workGroup.x
            val y = globalY - workGroup.y
            surface[(y * workGroup.w + x) * 3] = (Math.random() * 255).toByte()
            surface[(y * workGroup.w + x) * 3 + 1] = (Math.random() * 255).toByte()
            surface[(y * workGroup.w + x) * 3 + 2] = (Math.random() * 255).toByte()
            if (Thread.interrupted()) return
        }
        isReady.set(true)
    }
}

