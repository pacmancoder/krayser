package org.krayser.core

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * Main RayTracer control class
 * @param scene reference to scene, which will be traced
 * @param config data class with main ray tracer settings
 */
class KrayserController(scene: Scene, config: KrayserConfig) {

    var chunks: MutableList<Chunk> = arrayListOf()
    private var chunkTasks: MutableList<Future<*>> = arrayListOf()
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
                    val chunk = Chunk(ChunkRect(x, y, w, h))
                    chunks.add(chunk)
                    chunkTasks.add(threadPool.submit(chunk))
                }
            }
        }
    }
    fun abortTrace() {
        threadPool.shutdown()
        for (c in chunkTasks) {
            c.cancel(true)
        }
    }

    fun awaitTrace() {
        threadPool.shutdown()
        for (c in chunkTasks) {
            c.get()
        }
    }
}

