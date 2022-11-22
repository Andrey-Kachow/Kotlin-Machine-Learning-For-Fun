package ktml4fun.neuralnet

import ktml4fun.numerology.Matrix
import kotlin.math.exp
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.math.tanh

const val PI_F = 3.1415927F

typealias ActivationFunction = (x: Float) -> Float

class BinaryStepActivation : ActivationFunction {
    override fun invoke(x: Float): Float =
        if (x >= 0) {
            1.0F
        } else {
            0.0F
        }
}

class LinearActivation : ActivationFunction {
    override fun invoke(x: Float): Float {
        return x
    }
}

class SigmoidActivation : ActivationFunction {
    override fun invoke(x: Float): Float {
        return 1.0F / (1.0F + exp(-x))
    }
}

class TanhActivation : ActivationFunction {
    override fun invoke(x: Float): Float {
        return tanh(x)
    }
}

sealed class ParametricReLUActivation(
    private val parameter: Float
) : ActivationFunction {
    override fun invoke(x: Float): Float {
        return max(parameter * x, x)
    }
}

class ReLUActivation : ParametricReLUActivation(0.0F)

class LeakyReLUActivation : ParametricReLUActivation(0.1F)

class ExponentialLinearUnitActivation(
    private val parameter: Float
) : ActivationFunction {
    override fun invoke(x: Float): Float =
        if (x < 0.0F) {
            parameter * (exp(x) - 1)
        } else {
            x
        }
}

class SoftMaxActivation(input: Matrix) : ActivationFunction {

    private val sumOfExponentialValues: Float = input.sumOf(::exp)

    override fun invoke(x: Float): Float {
        return exp(x) / sumOfExponentialValues
    }
}

class SwishActivation : ActivationFunction {

    private val sigmoid: ActivationFunction = SigmoidActivation()

    override fun invoke(x: Float): Float {
        return x * sigmoid(x)
    }
}

class GaussianErrorLinearUnitActivation : ActivationFunction {
    override fun invoke(x: Float): Float =
        0.5F * x * (1F + tanh(sqrt(2F / PI_F) * (x * 0.044715F * x.pow(3F))))
}

class ScaledExponentialLinearUnit(
    alpha: Float,
    private val lambda: Float
) : ActivationFunction {

    private val elu = ExponentialLinearUnitActivation(alpha)

    override fun invoke(x: Float): Float = lambda * elu(x)
}
