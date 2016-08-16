package org.krayser.core

import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class Chunk(val workGroup: ChunkRect): Runnable {
    private var surfaceLock: Lock = ReentrantLock(true)
    var surface: ByteArray = ByteArray(workGroup.w * workGroup.h * 3)
        private set
        get() {
           surfaceLock.withLock {
               return field
           }
        }

    override fun run() {
        for ((x, y) in workGroup) {
            surfaceLock.withLock {
                surface[(y * workGroup.w + x) * 3] = (Math.random() * 255).toByte()
                surface[(y * workGroup.w + x) * 3 + 1] = (Math.random() * 255).toByte()
                surface[(y * workGroup.w + x) * 3 + 2] = (Math.random() * 255).toByte()
            }
            if (Thread.interrupted()) return
        }

    }
}

