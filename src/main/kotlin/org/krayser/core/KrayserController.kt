package org.krayser.core

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

/**
 * Main RayTracer control class
 */
class KrayserController(scene: Scene, config: KrayserConfig) {

    private var chunks: MutableList<Future<*>> = arrayListOf()
    private var threadPool: ExecutorService
    private var scene: Scene = scene
    private var width: Int = 800
    private var height: Int = 600
    init {
        threadPool = Executors.newFixedThreadPool(config.threads)
        width = config.imageSize.first
        height = config.imageSize.second
        with(config) {
            for (y in 0 until height step chunkSize) {
                for (x in 0 until width step chunkSize) {
                    val w = if (x + chunkSize > width) width % chunkSize else chunkSize
                    val h = if (y + chunkSize > height) height % chunkSize else chunkSize
                    chunks.add(threadPool.submit(Chunk(ChunkRect(x, y, w, h))))
                }
            }
        }
    }
    fun abortTrace() {
        // TODO: convert shutdown + awaitTermination to Extension function
        threadPool.shutdown()
        for (c in chunks) {
            c.cancel(true)
        }
        threadPool.awaitTermination(10, TimeUnit.SECONDS)
    }

    fun awaitTrace() {
        threadPool.shutdown()
        for (c in chunks) {
            c.get()
        }
        threadPool.awaitTermination(10, TimeUnit.SECONDS)
    }
}

