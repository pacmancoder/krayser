package org.krayser.core

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * Main RayTracer control class
 * @param scene reference to scene, which will be traced
 * @param config data class with main ray tracer settings
 */
class KrayserController(val scene: Scene, config: KrayserConfig) {

    var chunks: MutableList<Chunk> = arrayListOf()
    private var chunkTasks: MutableList<Future<*>> = arrayListOf()
    private var threadPool: ExecutorService
    val width: Int
    val height: Int

    init {
        threadPool = Executors.newFixedThreadPool(config.threads)
        width = config.imageSize.first
        height = config.imageSize.second
        scene.cam.rasterWidth = width.toFloat()
        scene.cam.rasterHeight = height.toFloat()
        with(config) {
            for (y in 0 until height step chunkSize) {
                for (x in 0 until width step chunkSize) {
                    val w = if (x + chunkSize > width) width % chunkSize else chunkSize
                    val h = if (y + chunkSize > height) height % chunkSize else chunkSize
                    val chunk = Chunk(this@KrayserController, ChunkRect(x, y, w, h))
                    chunks.add(chunk)
                }
            }
        }
    }

    fun startTrace() {
        for (chunk in chunks) {
            chunkTasks.add(threadPool.submit(chunk))
        }
    }
    /**
     * Abort all worker threads and finish tracing
     */
    fun abortTrace() {
        threadPool.shutdown()
        for (c in chunkTasks) {
            c.cancel(true)
        }
    }
}

