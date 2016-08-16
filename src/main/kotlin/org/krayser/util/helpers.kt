package org.krayser.util

/**
 * Returns next power of two value after `a`
 * Assumes that `a > 0`
 */
fun nextPowerOfTwo(a: Int) = 32 - Integer.numberOfLeadingZeros(a - 1)

