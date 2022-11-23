package ktml4fun.numerology

import kotlin.test.Test
import kotlin.test.assertEquals

internal class MatrixTest {

    private val identity3x3Matrix: Matrix = Matrix.of(
        1F, 0F, 0F,
        0F, 1F, 0F,
        0F, 0F, 1F
    ).resizedAs(3, 3)

    private val sample3x3MatrixA: Matrix = Matrix.of(
        3F, 2F, 9F,
        2F, 1F, 8F,
        4F, 5F, 2F
    ).resizedAs(3, 3)

    private val sample3x3MatrixB: Matrix = Matrix.of(
        0F, 9F, 4F,
        3F, 1F, 4F,
        1F, 2F, 0F
    ).resizedAs(3, 3)

    private val sample3x3MatrixAB: Matrix = Matrix.of(
        15F, 47F, 20F,
        11F, 35F, 12F,
        17F, 45F, 36F
    ).resizedAs(3, 3)

    @Test
    fun `Matrix 3x3 multiplication with Identity matrix`() {
        assert(identity3x3Matrix x sample3x3MatrixA == sample3x3MatrixA)
        assert(sample3x3MatrixA x identity3x3Matrix == sample3x3MatrixA)
    }

    @Test
    fun `Matrix 3x3 by 3x3 multiplication`() {
        assertEquals(sample3x3MatrixAB, sample3x3MatrixA x sample3x3MatrixB)
    }
}
