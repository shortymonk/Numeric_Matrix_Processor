package processor

import java.text.DecimalFormat
import kotlin.math.pow

class MatrixBuilder {

    data class Matrix(val rows: Int, val columns: Int) {

        val matrix: List<List<Double>> = List(rows) { readLine()!!.split(" ").map { it.toDouble() }.toList() }
        val determinant by lazy {
            MatrixBuilder().calcDeterminant(matrix).first
        }
        val coMatrix by lazy {
            MatrixBuilder().calcAdjoin(matrix).second
        }
        val inverseMatrix by lazy {
            MatrixBuilder().getInverseMatrix(coMatrix, determinant)
        }
        val transposedMain by lazy {
            MatrixBuilder().getMain(this.matrix)
        }
        val transposedSide by lazy {
            MatrixBuilder().getSide(this)
        }
        val transposedVertical by lazy {
            MatrixBuilder().getVertical(this)
        }
        val transposedHorizontal by lazy {
            MatrixBuilder().getHorizontal(this)
        }
    }

    fun printMatrix(m: List<List<Double>>) {
        val output = mutableListOf<MutableList<String>>()
        val maxLength = MutableList(m.first().size) { 0 }
        for (row in m.indices) {
            val line = mutableListOf<String>()
            for (column in m[row].indices) {
                if (m[row][column] == 0.0) {
                    line.add("0")
                } else {
                    val num = m[row][column]
                    val scalar = DecimalFormat("0.0000").format(num).dropLast(2).replace(".00", "")
                    line.add(scalar)
                    if (scalar.length > maxLength[column]) maxLength[column] = scalar.length
                }
            }
            output.add(line)
        }
        for (i in output.indices) {
            for (j in output[i].indices) {
                if (output[i][j].length < maxLength[j]) {
                    output[i][j] = " ".repeat(maxLength[j] - output[i][j].length) + output[i][j]
                }
            }
        }
        output.forEach { println(it.joinToString(" ")) }
    }

    private fun calcDeterminant(matrix: List<List<Double>>): Pair<Double, List<Double>> {
        if (matrix.size != matrix[0].size) {
            println("The operation cannot be performed.")
            return 0.0 to emptyList()
        }
        var result = 0.0
        val cofactor = mutableListOf<Double>()
        if (matrix.size == 2) {
            return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0] to emptyList()
        } else {
            var sign = 1.0
            for (i in matrix.indices) {
                val scalar = matrix[0][i]
                val cutMatrix = mutableListOf<MutableList<Double>>()
                for (j in 1..matrix.lastIndex){
                    cutMatrix.add(matrix[j].toMutableList())
                }
                cutMatrix.forEach { it.removeAt(i) }
                cofactor.add(calcDeterminant(cutMatrix).first * sign)
                result += scalar * cofactor.last()
                sign *= -1.0
            }
            return result to cofactor
        }
    }

    private fun calcAdjoin(m: List<List<Double>>): Pair<Double, List<List<Double>>> {
        val output = mutableListOf<List<Double>>()
        return if (m.size == 2) {
            m[0][0] * m[1][1] - m[0][1] * m[1][0] to emptyList()
        } else {
            for (row in m.indices) {
                val line = mutableListOf<Double>()
                for (column in m.indices){
                    val coMatrix = mutableListOf<MutableList<Double>>()
                    m.filterIndexed{ index, _ -> index !=row  }
                        .forEach { coMatrix.add( it.filterIndexed { index, _ -> index != column }.toMutableList()) }
                    line.add(calcDeterminant(coMatrix).first * (-1.0).pow(row + column))
                }
                output.add(line)
            }
            0.0 to getMain(output)
        }
    }

    private fun getInverseMatrix(m: List<List<Double>>, determinant: Double): List<List<Double>> {
        if (determinant == 0.0) {
            println("This matrix doesn't have an inverse.")
            return emptyList()
        }
        val output = mutableListOf<MutableList<Double>>()
        m.forEach { output.add(it.toMutableList()) }
        output.forEach {
            for (i in it.indices) {
                it[i] = it[i] / determinant
            }
        }
        return output
    }

    private fun getMain(matrixObject: List<List<Double>>): List<List<Double>> {
        val matrix = matrixObject.toList()
        val size = matrix.size
        val result = List(size) { MutableList(size) { 0.0 } }
        for (row in matrix.indices) {
            for (column in matrix.indices) {
                result[column][row] = matrix[row][column]
            }
        }
        return  result
    }

    private fun getSide(matrixObject: Matrix): List<List<Double>> {
        val result = List(matrixObject.rows) { MutableList(matrixObject.columns) { 0.0 } }
        for (row in matrixObject.matrix.indices) {
            for (column in matrixObject.matrix.indices) {
                result[column][row] = matrixObject.matrix.reversed()[row].reversed()[column]
            }
        }
        return result
    }

    private fun getVertical(matrixObject: Matrix): List<List<Double>> {
        val result = mutableListOf<List<Double>>()
        matrixObject.matrix.forEach {
            result.add(it.reversed())
        }
        return result
    }

    private fun getHorizontal(matrixObject: Matrix): List<List<Double>> {
        return matrixObject.matrix.reversed()
    }

    fun add(a: Matrix, b: Matrix): List<List<Double>> {
        if (a.rows != b.rows && a.columns != b.columns) {
            println("The operation cannot be performed.")
            return emptyList()
        }
        val result = mutableListOf<MutableList<Double>>()
        for (row in a.matrix.indices) {
            val line = mutableListOf<Double>()
            for (column in a.matrix[row].indices) {
                line.add(a.matrix[row][column] + b.matrix[row][column])
            }
            result.add(line)
        }
        return result
    }

    private fun getColumn(matrix: List<List<Double>>, index: Int): List<Double> {
        val output = mutableListOf<Double>()
        matrix.forEach { output.add(it[index]) }
        return output
    }

    private fun dotProduct(a: List<Double>, b: List<Double>): Double {
        var dotProduct = 0.0
        for (i in a.indices) { dotProduct += a[i] * b[i] }
        return dotProduct
    }

    fun multiply(a: Matrix, b: Matrix): List<List<Double>> {
        if (a.columns != b.rows && b.rows * b.columns != 1) {
            println("The operation cannot be performed.")
            return emptyList()
        } else {
            if (b.rows * b.columns == 1) {
                val result = mutableListOf<MutableList<Double>>()
                val matrixA = a.matrix

                for (row in matrixA) {
                    val line = mutableListOf<Double>()
                    row.forEach { line.add(it * b.matrix[0][0]) }
                    result.add(line)
                }

                return result
            } else {
                val result = MutableList(a.rows) { MutableList(b.columns) { 0.0 } }
                val matrixA = a.matrix
                val matrixB = b.matrix
                for (i in matrixA.indices) {
                    for (j in result[i].indices) {
                        result[i][j] = dotProduct(matrixA[i], getColumn(matrixB, j))
                    }
                }
                return result
            }
        }
    }

}