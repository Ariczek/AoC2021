data class OneEntry(
    var value: Int,
    var marked: Boolean
)

private fun parseDrawnNumbers(input: String): List<Int> {
    val list = input.split(",")
    return list.map { Integer.parseInt(it) }
}

private fun parseBoard(input: List<String>) : Array<Array<OneEntry>> {
    var arr = input
    var ret: Array<Array<OneEntry>> = emptyArray()

    // if first line is empty, just remove it
    if (arr[0].isEmpty()) {
        arr = arr.subList(1, arr.size)
    }

    // each board contains 5 lines with 5 values
    arr.subList(0, 5).forEach { line ->
        val array: Array<OneEntry> = Array(5) { OneEntry(value = 0, marked = false) }
        val asList = line.chunked(3).map { Integer.parseInt(it.trim()) }
        for (i in array.indices) {
            array[i].value = asList[i]
        }

        ret = ret.plus(array)
    }

    return ret
}

private fun updateBoardsByDrawnNumber(boards: MutableList<Array<Array<OneEntry>>>, number: Int): MutableList<Array<Array<OneEntry>>>{
    boards.forEach { board ->
        board.forEach { line ->
            line.forEach { entry ->
                if (entry.value == number) {
                    entry.marked = true
                }
            }
        }
    }

    return boards
}

//private fun printBoards(boards: List<Array<Array<OneEntry>>>) {
//    boards.forEach { board ->
//        board.forEach { line ->
//            println(line.joinToString())
//        }
//        println()
//    }
//}

private fun checkBoardWin(board: Array<Array<OneEntry>>): Boolean {
    // two conditions how a board can win, either all numbers in a single row are marked or in a single column
    // row check is easier
    board.forEach { line ->
        if (line.all { it.marked }) return true
    }

    // column check
    for (i in 0 until board[0].size) {
        if (board.all { it[i].marked }) return true
    }

    return false
}

private fun getSumForBoardUnmarked(board: Array<Array<OneEntry>>): Int {
    var sum = 0
    board.forEach { line ->
        line.forEach { entry ->
            if (!entry.marked) {
                sum += entry.value
            }
        }
    }
    return sum
}

fun part1(input: List<String>): Int {

    val drawnNumbers = parseDrawnNumbers(input[0])
    var boardList: MutableList<Array<Array<OneEntry>>> = mutableListOf()

    for (i in 1 until input.size step 6) {
        boardList.add(parseBoard(input.subList(i, i+6)))
    }

    drawnNumbers.forEach { drawnNumber ->
        boardList = updateBoardsByDrawnNumber(boardList, drawnNumber)
        boardList.forEach { board ->
            if (checkBoardWin(board)) {
                return getSumForBoardUnmarked(board) * drawnNumber
            }
        }
    }

    return -1
}

fun part2(input: List<String>): Int {
    val drawnNumbers = parseDrawnNumbers(input[0])
    var boardList: MutableList<Array<Array<OneEntry>>> = mutableListOf()

    for (i in 1 until input.size step 6) {
        boardList.add(parseBoard(input.subList(i, i+6)))
    }

    drawnNumbers.forEach { drawnNumber ->
        boardList = updateBoardsByDrawnNumber(boardList, drawnNumber)

        // remove all boards, which would win this round
        val tmpBoardList = boardList.filter { board ->
            !checkBoardWin(board)
        }

        if (tmpBoardList.isEmpty() && boardList.size == 1) {
            return getSumForBoardUnmarked(boardList[0]) * drawnNumber
        }

        boardList = tmpBoardList.toMutableList()
    }

    return -1
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}