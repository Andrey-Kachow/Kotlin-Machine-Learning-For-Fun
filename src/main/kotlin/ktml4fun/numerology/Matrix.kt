package ktml4fun.numerology

class Matrix private constructor(
    numberOfRows: Int,
    numberOfColumns: Int
) {
    val rows: Int = numberOfRows
    val cols: Int = numberOfColumns

    private val entries: FloatArray = FloatArray(rows * cols)

    private constructor(
        numberOfRows: Int,
        numberOfColumns: Int,
        values: FloatArray
    ) : this(
        numberOfRows,
        numberOfColumns
    ) {
        require(numberOfRows * numberOfColumns == values.size)
        values.forEachIndexed { index, value ->
            entries[index] = value
        }
    }

    operator fun get(row: Int, col: Int): Float {
        return entries[row * cols + col]
    }

    operator fun set(row: Int, col: Int, value: Float) {
        entries[row * cols + col] = value
    }

    private fun forEachIndexed(action: (row: Int, col: Int, value: Float) -> Unit) {
        entries.forEachIndexed { index, entry ->
            action(index / cols, index % cols, entry)
        }
    }

    fun map(transform: (Float) -> Float): Matrix {
        return Matrix(cols, rows).also {
            forEachIndexed { row, col, value ->
                it[row, col] = transform(value)
            }
        }
    }

    fun sumOf(transform: (Float) -> Float): Float {
        return map(transform).sum()
    }

    private fun sum(): Float {
        return entries.sum()
    }

    fun transpose(): Matrix {
        return Matrix(cols, rows).also {
            forEachIndexed { row, col, value ->
                it[col, row] = value
            }
        }
    }

    private fun elementWiseBinaryOperationWith(
        other: Matrix,
        operation: (x: Float, y: Float) -> Float
    ): Matrix {
        require(this.rows == other.rows && this.cols == other.cols)
        return Matrix(cols, rows).also {
            forEachIndexed { row, col, value ->
                it[row, col] = operation(value, other[row, col])
            }
        }
    }

    fun plus(other: Matrix): Matrix {
        return elementWiseBinaryOperationWith(other) { x, y -> x + y }
    }

    fun minus(other: Matrix): Matrix {
        return elementWiseBinaryOperationWith(other) { x, y -> x - y }
    }

    private fun matrixMultiplyBy(other: Matrix): Matrix {
        require(this.cols == other.rows)
        val joinSize = this.cols
        return Matrix(this.rows, other.cols).also {
            it.forEachIndexed { destRow, destCol, _ ->
                var dotProduct = 0.0F
                for (index in 0 until joinSize) {
                    dotProduct += this[destRow, index] * other[index, destCol]
                }
                it[destRow, destCol] = dotProduct
            }
        }
    }

    infix fun o(other: Matrix): Matrix {
        return matrixMultiplyBy(other)
    }

    fun elementWiseMultiplyBy(other: Matrix): Matrix {
        return elementWiseBinaryOperationWith(other) { x, y -> x * y }
    }

    fun copy(): Matrix {
        return Matrix(rows, cols, entries)
    }

    fun resizedAs(numberOfRows: Int, numberOfColumns: Int): Matrix {
        require(numberOfRows * numberOfColumns == rows * cols)
        return Matrix(numberOfRows, numberOfColumns, entries)
    }

    override fun equals(other: Any?): Boolean {
        return other is Matrix &&
            this.rows == other.rows &&
            this.cols == other.cols &&
            this.entries.contentEquals(other.entries)
    }

    fun kotlinHashCode(): Int {
        val hashMagic = 31
        return rows
            .let {
                hashMagic * it + cols
            }.let {
                hashMagic * it * entries.contentHashCode()
            }
    }

    override fun hashCode(): Int {
        val hashMagic = 31
        var result = rows
        result = hashMagic * result + cols
        result = hashMagic * result + entries.contentHashCode()
        return result
    }

    companion object {
        fun empty(numberOfRows: Int, numberOfColumns: Int): Matrix {
            return Matrix(numberOfRows, numberOfColumns)
        }

        fun of(vararg values: Float): Matrix {
            return Matrix(values.size, 1).apply {
                values.forEachIndexed { index, value ->
                    entries[index] = value
                }
            }
        }

        fun columnVector(size: Int): Matrix {
            return Matrix(size, 1)
        }

        fun columnVector(vararg values: Float): Matrix {
            return of(*values).resizedAs(values.size, 1)
        }
    }
}
