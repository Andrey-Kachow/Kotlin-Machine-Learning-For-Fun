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

    operator fun get(row: Int, col: Int): Float = entries[row * cols + col]

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

    fun sumOf(transform: (Float) -> Float): Float = map(transform).sum()

    fun sum(): Float = entries.sum()

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

    operator fun plus(other: Matrix): Matrix =
        elementWiseBinaryOperationWith(other) { x, y -> x + y }

    operator fun minus(other: Matrix): Matrix =
        elementWiseBinaryOperationWith(other) { x, y -> x - y }

    operator fun minus(scalar: Float): Matrix = map { it - scalar }

    operator fun times(scalar: Float): Matrix = map { it * scalar }

    operator fun unaryMinus(): Matrix = map { -it }

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

    fun elementWiseMultiplyBy(other: Matrix): Matrix =
        elementWiseBinaryOperationWith(other) { x, y -> x * y }

    infix fun x(other: Matrix): Matrix = matrixMultiplyBy(other)

    infix fun o(other: Matrix): Matrix = elementWiseMultiplyBy(other)

    fun copy(): Matrix = Matrix(rows, cols, entries)

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

    override fun hashCode(): Int =
        31 * (31 * rows + cols) * entries.contentHashCode()

    /**
     * returns Matrix representation in desired form. For example:
     * ```
     * Matrix(3x3) {
     *     1 2 3
     *     4 5 6
     *     7 8 9
     * }
     */
    override fun toString(): String {
        val sb = StringBuilder("Matrix(${rows}x$cols) {\n")
        for (i in 0 until rows) {
            sb.append("\t")
            for (j in 0 until cols) {
                sb.append("${get(i, j)} ")
            }
            sb.append("\n")
        }
        sb.append("}\n")
        return sb.toString()
    }

    companion object {
        val empty = ::ofSize

        fun ofSize(numberOfRows: Int, numberOfColumns: Int): Matrix =
            Matrix(numberOfRows, numberOfColumns)

        fun of(vararg values: Float): Matrix {
            return Matrix(values.size, 1).apply {
                values.forEachIndexed { index, value ->
                    entries[index] = value
                }
            }
        }

        fun columnVector(size: Int): Matrix = Matrix(size, 1)

        fun columnVector(vararg values: Float): Matrix {
            return of(*values).resizedAs(values.size, 1)
        }
    }
}

operator fun Float.times(matrix: Matrix): Matrix = matrix * this
