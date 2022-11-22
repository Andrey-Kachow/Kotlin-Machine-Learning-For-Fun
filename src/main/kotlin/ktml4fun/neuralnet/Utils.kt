package ktml4fun.neuralnet

import ktml4fun.numerology.Matrix
import kotlin.math.sqrt
import kotlin.random.Random

fun xavierInitWeights(
    inputSize: Int,
    outputSize: Int,
    gain: Float = 1F
): Matrix {
    val maxWeightValue = gain * sqrt(6F / (inputSize + outputSize))
    return Matrix.ofSize(inputSize, outputSize)
        .map {
            Random.nextFloat() * 2F * maxWeightValue - maxWeightValue
        }
}
