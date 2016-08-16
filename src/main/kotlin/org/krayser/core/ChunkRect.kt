package org.krayser.core

data class ChunkRect(val x: Int, val y: Int, val w: Int, val h: Int) : Iterable<Pair<Int, Int>> {
    inner class ChunkRectIterator: Iterator<Pair<Int, Int>> {
        var pixelCounter = 0

        override fun hasNext(): Boolean = pixelCounter < w * h

        override fun next(): Pair<Int, Int> {
            val last = pixelCounter
            pixelCounter++
            return Pair(last % w, last / w)
        }
    }
    override fun iterator(): Iterator<Pair<Int, Int>> = ChunkRectIterator()
}


