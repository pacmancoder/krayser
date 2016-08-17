package org.krayser.util

/**
 * Standard 4x4 Transformation matrix class
 */
class TransMatrix(var data: FloatArray) {
    companion object {
    /**
     * Returns identity matrix
     */
    fun identity(): TransMatrix = TransMatrix(floatArrayOf(
            1f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f,
            0f, 0f, 1f, 0f,
            0f, 0f, 0f, 1f))

    /**
     * Returns zero matrix
     */
    fun zero(): TransMatrix = TransMatrix(floatArrayOf(
            0f, 0f, 0f, 0f,
            0f, 0f, 0f, 0f,
            0f, 0f, 0f, 0f,
            0f, 0f, 0f, 0f))
    }

    /*
    mul, transform, translate, rotateX/Y/Z, scale
     */
}