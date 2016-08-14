package org.krayser.core

class Chunk(val workGroup: ChunkRect): Runnable {
    override fun run() {
        try {
            Thread.sleep(50)
        } catch (e: InterruptedException) {
            println("Thread at ${workGroup.x}:${workGroup.y} interrupted!")
        }
        println("Thread at ${workGroup.x}:${workGroup.y} finished")
    }
}

