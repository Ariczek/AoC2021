private data class Coordinates(
    val yCoord: Int,
    val xCoord: Int
)

private var totalCount = 0
private var flashCount = 0

private fun simulateOneStep(input: MutableList<MutableList<Int>>): MutableList<MutableList<Int>> {
    val flashingPoints: MutableList<Coordinates> = mutableListOf()
    flashCount = 0

    // first increment all octopus by 1,
    input.forEachIndexed { lineNum, line ->
        line.forEachIndexed { columnNum, _ ->
            input[lineNum][columnNum]++
            when (input[lineNum][columnNum]) {
                10 -> {
                    flashingPoints.add(Coordinates(lineNum, columnNum))
                }
            }
        }
    }

    while (flashingPoints.isNotEmpty()) {
        val point = flashingPoints.removeFirst()

        for (i in -1 .. 1) {
            for (j in -1 .. 1) {
                if (i != 0 || j != 0) {
                    if (((point.xCoord + i) in 0 until input[0].size) && ((point.yCoord + j) in 0 until input.size)) {
                        input[point.yCoord + j][point.xCoord + i]++
                        when (input[point.yCoord + j][point.xCoord + i]) {
                            10 -> {
                                flashingPoints.add(Coordinates(point.yCoord + j, point.xCoord + i))
                            }
                        }
                    }
                }
            }
        }
    }

    input.forEachIndexed { lineNum, line ->
        line.forEachIndexed { columnNum, _ ->
            when (input[lineNum][columnNum] >= 10) {
                true -> {
                    input[lineNum][columnNum] = 0
                    totalCount ++
                    flashCount ++
                }
            }
        }
    }

    return input
}

private fun part1(input: List<String>): Int {

    var map: MutableList<MutableList<Int>> = mutableListOf()
    input.forEach { line ->
        map.add(line.trim().chunked(1).map { Integer.parseInt(it) }.toMutableList())
    }

    totalCount = 0
    for (i in 1 .. 100) {
        map = simulateOneStep(map)
    }

    return totalCount
}

private fun part2(input: List<String>): Int {
    var map: MutableList<MutableList<Int>> = mutableListOf()
    input.forEach { line ->
        map.add(line.trim().chunked(1).map { Integer.parseInt(it) }.toMutableList())
    }

    var step = 0
    while (flashCount < 100) {
        step ++
        map = simulateOneStep(map)
    }

    return step
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 1656)
    check(part2(testInput) == 195)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}