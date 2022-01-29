package processor

typealias Matrix = MatrixBuilder.Matrix
fun main() {
    val calc = MatrixBuilder()
    val resultIs = { println("Result is: ") }
    val menu = """
        1. Add matrices
        2. Multiply matrix by a constant
        3. Multiply matrices
        4. Transpose matrix
        5. Calculate a determinant
        6. Inverse matrix
        0. Exit
    """.trimIndent()

    while (true) {
        println(menu)
        print("Your choice: ")
        var choice = readLine()!!
        when (choice) {
            "1" -> {
                println("Enter size of first matrix: ")
                val (rowsA, columnsA) = createMatrix()
                println("Enter first matrix: ")
                val a = Matrix(rowsA, columnsA)
                println("Enter size of second matrix: ")
                val (rowsB, columnsB) = createMatrix()
                println("Enter second matrix: ")
                val b = Matrix(rowsB, columnsB)
                if (rowsA == rowsB && columnsA == columnsB) {
                    val c = calc.add(a, b)
                    resultIs()
                    calc.printMatrix(c)
                } else {
                    println("The operation cannot be performed.")
                    continue
                }
            }
            "2" -> {
                print("Enter size of matrix: ")
                val (rowsA, columnsA) = createMatrix()
                println("Enter matrix: ")
                val a = Matrix(rowsA, columnsA)
                print("Enter constant: ")
                val scalar = Matrix(1, 1)
                resultIs()
                calc.printMatrix(calc.multiply(a, scalar))
            }
            "3" -> {
                println("Enter size of first matrix: ")
                val (rowsA, columnsA) = createMatrix()
                println("Enter first matrix: ")
                val a = Matrix(rowsA, columnsA)
                println("Enter size of second matrix: ")
                val (rowsB, columnsB) = createMatrix()
                println("Enter second matrix: ")
                val b = Matrix(rowsB, columnsB)
                val c = calc.multiply(a, b)
                if (c.isNotEmpty()) {
                    resultIs()
                    calc.printMatrix(c)
                }
            }
            "4" -> {
                val menuTranspose = """
                    1. Main diagonal
                    2. Side diagonal
                    3. Vertical line
                    4. Horizontal line
                """.trimIndent()
                println(menuTranspose)
                print("Your choice: ")
                choice = readLine()!!
                print("Enter size of matrix: ")
                val (rowsA, columnsA) = createMatrix()
                println("Enter matrix: ")
                val a = Matrix(rowsA, columnsA)
                when (choice) {
                    "1" -> calc.printMatrix(a.transposedMain).also { resultIs() }
                    "2" -> calc.printMatrix(a.transposedSide).also { resultIs() }
                    "3" -> calc.printMatrix(a.transposedVertical).also { resultIs() }
                    "4" -> calc.printMatrix(a.transposedHorizontal).also { resultIs() }
                }
            }
            "5" -> {
                print("Enter size of matrix: ")
                val (rowsA, columnsA) = createMatrix()
                println("Enter matrix: ")
                val a = Matrix(rowsA, columnsA)
                if (a.coMatrix.isNotEmpty()) println(a.determinant)
            }
            "6" -> {
                print("Enter size of matrix: ")
                val (rowsA, columnsA) = createMatrix()
                println("Enter matrix: ")
                val a = Matrix(rowsA, columnsA)
                if (a.determinant == 0.0) {
                    println("This matrix doesn't have an inverse.")
                } else {
                    resultIs()
                    a.matrix
                    a.coMatrix
                    calc.printMatrix(a.inverseMatrix)
                }
            }
            "0" -> break
        }
    }
}

fun createMatrix() = readLine()!!.split(" ").map { it.toInt() }


