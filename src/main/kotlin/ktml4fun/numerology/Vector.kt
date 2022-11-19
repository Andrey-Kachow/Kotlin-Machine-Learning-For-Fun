package ktml4fun.numerology

class Vector(
    val dimension: Int
) {
    private val values: FloatArray = FloatArray(dimension)

    constructor(vararg numbers: Float) : this(numbers.size) {
        numbers.forEachIndexed { index, number ->
            values[index] = number
        }
    }

    fun get(index: Int): Float {
        return values[index]
    }

    fun set(index: Int, value: Float) {
        values[index] = value
    }
}
