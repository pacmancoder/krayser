package org.krayser.frontend

import org.krayser.core.Chunk

interface Frontend {
    fun changeTitle(title: String)
    fun drawChunk(chunk: Chunk)
}