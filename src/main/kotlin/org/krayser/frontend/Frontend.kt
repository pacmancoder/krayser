package org.krayser.frontend

import org.krayser.core.Chunk

/**
 * Interface for Frontend representation
 */
interface Frontend {
    /**
     * Changes windows title
     */
    fun changeTitle(title: String)

    /**
     * Clears drawing surface (like window)
     */
    fun clearSurface()
    /**
     * Draws Chunk surface
     */
    fun drawChunk(chunk: Chunk)
}