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

        fun translate(v: Vec3D): TransMatrix = TransMatrix(floatArrayOf(
                1f, 0f, 0f, v.x,
                0f, 1f, 0f, v.y,
                0f, 0f, 1f, v.z,
                0f, 0f, 0f, 1f))

        fun scale(v: Vec3D): TransMatrix = TransMatrix(floatArrayOf(
                v.x, 0f,  0f,  0f,
                0f,  v.y, 0f,  0f,
                0f,  0f,  v.z, 0f,
                0f,  0f,  0f,  1f))

        fun rotateX(angle: Float): TransMatrix {
            val (cos, sin) = cosSin(angle)
            return TransMatrix(floatArrayOf(
                    1f, 0f,  0f,  0f,
                    0f, cos, sin,  0f,
                    0f, -sin,  cos, 0f,
                    0f,  0f,  0f,  1f))
        }

        fun rotateY(angle: Float): TransMatrix {
            val (cos, sin) = cosSin(angle)
            return TransMatrix(floatArrayOf(
                    cos, 0f,  -sin,  0f,
                    0f,  1f, 0f,  0f,
                    sin,  0f,  cos, 0f,
                    0f,  0f,  0f,  1f))
        }

        fun rotateZ(angle: Float): TransMatrix {
            val (cos, sin) = cosSin(angle)
            return TransMatrix(floatArrayOf(
                    cos, -sin,  0f,  0f,
                    sin,  cos, 0f,  0f,
                    0f,  0f,  1f, 0f,
                    0f,  0f,  0f,  1f))
        }

    }
    /**
     * Matrix multiplication
     */
    operator fun times(rhs: TransMatrix): TransMatrix {
        val m = TransMatrix.zero()
        for (y in 0..3) {
            for (x in 0..3) {
                var cell = 0f
                for (n in 0..3) {
                    cell += this.data[y * 4 + n] * rhs.data[n * 4 + x]
                }
                m.data[y * 4 + x] = cell
            }
        }
        return m
    }
    /*
        transform, translate, rotateX/Y/Z, scale
     */
}