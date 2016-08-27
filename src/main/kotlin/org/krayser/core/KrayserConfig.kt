package org.krayser.core

const val defaultWidth = 1280
const val defaultHeight = 1024
const val defaultChunkSize = 32

/**
 * Ray tracer settings data class
 * @param threads available to thread pool threads count. By default equals to machine's logical threads count
 * @param imageSize **Pair<Int, Int>** which represents **(width, height)** tuple. Default is 800x600 image
 * @param chunkSize size of square area, which represents single tast for a thread. Default is 32 pixels
 */
data class KrayserConfig(val threads: Int = Runtime.getRuntime().availableProcessors(),
                         val imageSize: Pair<Int, Int> = Pair(defaultWidth, defaultHeight),
                         val chunkSize: Int = defaultChunkSize)