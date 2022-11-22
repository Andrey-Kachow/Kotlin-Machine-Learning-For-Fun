package ktml4fun.neuralnet

import ktml4fun.numerology.Matrix
import ktml4fun.numerology.times

class Layer(
    private val inputSize: Int,
    private val outputSize: Int,
    private val activation: ActivationFunction
) {
    private var bias: Matrix = Matrix.columnVector(outputSize)
    private var weights: Matrix = xavierInitWeights(inputSize, outputSize)

    private var weightGradients: Matrix = Matrix.empty(inputSize, outputSize)
    private var biasGradients: Float = 0F

    private var backwardInput: Matrix = Matrix.empty(1, 1)

    fun forward(input: Matrix): Matrix {
        backwardInput = input.transpose()
        // TODO: Apply activation function
        return (weights x input) + bias
    }

    fun backward(outputLosses: Matrix): Matrix {
        weightGradients = backwardInput x outputLosses
        biasGradients = outputLosses.sum()
        return weights.transpose() x outputLosses
    }

    fun update(learningRate: Float) {
        weights -= learningRate * weightGradients
        bias -= learningRate * biasGradients
    }
}
