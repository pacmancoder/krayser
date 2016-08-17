package org.krayser.util

/**
 * 3D Vector
 */
data class Vec3D(var x: Float, var y: Float, var z: Float) {
    /**
     * Constructs vector with scalar
     */
    constructor(scalar: Float): this(scalar, scalar, scalar)
    constructor(vec: Vec2D, scalar: Float): this(vec.x, vec.y, scalar)
    constructor(scalar: Float, vec: Vec2D): this(scalar, vec.x, vec.y)

    /**
     * Returns length of vector
     */
    fun len() = Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()

    /**
     * Returns normalized vector
     */
    fun normalized() = this / len()

    /**
     * Reflects vector from palne, defined by [normal]
     * @param normal plane normal
     */
    fun reflect(normal: Vec3D) = this - 2f * (this dot normal) * normal

    /**
     * Returns projection of 3D vector on 2D plane
     */
    fun
            project() = Vec2D(x, y)
    /**
     * Returns dot product of vectors
     */
    infix fun dot(rhs: Vec3D) = x * rhs.x + y * rhs.y + z * rhs.z

    /**
     * Returns cross-product of vectors
     */
    infix fun cross(rhs: Vec3D) = Vec3D(
            +(rhs.y * z - rhs.z * y),
            -(rhs.x * z - rhs.z * x),
            +(rhs.x * y - rhs.y * x))

    /**
     * multiplies vector with scalar
     */
    operator fun times(scalar: Float) = Vec3D(x * scalar, y * scalar, z * scalar)

    /**
     * divides vector with scalar
     */
    operator fun div(scalar: Float) = Vec3D(x / scalar, y / scalar, z / scalar)

    /**
     * Returns difference between vectors
     */
    operator fun minus(rhs: Vec3D) = Vec3D(x - rhs.x, y - rhs.y, z - rhs.z)

    /**
     * Returns sum of vectors
     */
    operator fun plus(rhs: Vec3D) = Vec3D(x + rhs.x, y + rhs.y, z + rhs.z)

    /**
     * Returns inverted vector
     */
    operator fun unaryMinus() = Vec3D(-x, -y, -z)
    /**
     * Index operator
     */
    operator fun get(index: Int): Float = when (index) {
        0 -> x
        1 -> y
        2 -> z
        else -> throw IndexOutOfBoundsException("Vector3D have only 3 components")
    }
}

/**
 * multiplies vector by float
 */
operator fun Float.times(vec: Vec3D) = vec * this

/**
 * divides vector by float
 */
operator fun Float.div(vec: Vec3D) = vec / this