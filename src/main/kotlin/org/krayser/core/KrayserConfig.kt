package org.krayser.core

const val defaultWidth = 800
const val defaultHeight = 600
const val defaultChunkSize = 32

data class KrayserConfig(val threads: Int = Runtime.getRuntime().availableProcessors(),
                         val imageSize: Pair<Int, Int> = Pair(defaultWidth, defaultHeight),
                         val chunkSize: Int = defaultChunkSize)