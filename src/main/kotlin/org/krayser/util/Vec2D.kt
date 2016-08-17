package org.krayser.util

/**
 * 2D Vector
 */
data class Vec2D(var x: Float, var y: Float) {
    /**
     * Constructs vector with scalar
     */
    constructor(scalar: Float): this(scalar, scalar)

    /**
     * Returns length of vector
     */
    fun len() = Math.sqrt((x * x + y * y).toDouble()).toFloat()

    /**
     * Returns normalized vector
     */
    fun normalized() = this / len()

    /**
     * Reflects vector from palne, defined by [normal]
     * @param normal plane normal
     */
    fun reflect(normal: Vec2D) = this - 2f * (this dot normal) * normal
    /**
     * Returns dot product of vectors
     */
    infix fun dot(rhs: Vec2D) = x * rhs.x + y * rhs.y

    /**
     * multiplies vector with scalar
     */
    operator fun times(scalar: Float) = Vec2D(x * scalar, y * scalar)

    /**
     * divides vector with scalar
     */
    operator fun div(scalar: Float) = Vec2D(x / scalar, y / scalar)

    /**
     * Returns difference between vectors
     */
    operator fun minus(rhs: Vec2D) = Vec2D(x - rhs.x, y - rhs.y)

    /**
     * Returns sum of vectors
     */
    operator fun plus(rhs: Vec2D) = Vec2D(x + rhs.x, y + rhs.y)

    /**
     * Returns inverted vector
     */
    operator fun unaryMinus() = Vec2D(-x, -y)
    /**
     * Index operator
     */
    operator fun get(index: Int): Float = when (index) {
        0 -> x
        1 -> y
        else -> throw IndexOutOfBoundsException("Vector2D have only 2 components")
    }
}

/**
 * multiplies vector by float
 */
operator fun Float.times(vec: Vec2D) = vec * this

/**
 * divides vector by float
 */
operator fun Float.div(vec: Vec2D) = vec / this