private enum class BracketType {
    ROUND, SQUARE, CURLY, ANGLE, EMPTY;
}

private fun getValueFor1(input: BracketType): Int {
    return when(input) {
        BracketType.ROUND -> 3
        BracketType.SQUARE -> 57
        BracketType.CURLY -> 1197
        BracketType.ANGLE -> 25137
        BracketType.EMPTY -> 0
    }
}

private fun identifyBracketType(input: Char): BracketType {
    return when (input) {
        '(',')' -> BracketType.ROUND
        '[',']' -> BracketType.SQUARE
        '{','}' -> BracketType.CURLY
        '<','>' -> BracketType.ANGLE
        else -> BracketType.EMPTY
    }
}

private fun isBracketOpening(input: Char) : Boolean{
    return when (input) {
        '(','[','{','<' -> true
        else -> false
    }
}

private fun part1(input: List<String>): Int {
    var sum = 0

    input.forEach { line ->
        val brackets = line.trim().chunked(1).map { it[0] }

        val readOpened: MutableList<BracketType> = mutableListOf()

        for (i in brackets.indices) {
            val type = identifyBracketType(brackets[i])

            when(isBracketOpening(brackets[i])) {
                true -> readOpened.add(type)
                false -> {
                    if (readOpened.isNotEmpty() && readOpened.last() == type) {
                        readOpened.removeLast()
                    } else {
                        sum += getValueFor1(type)
                        break
                    }
                }
            }
        }
    }

    return sum
}

private fun getValueFor2(input: BracketType): Int {
    return when(input) {
        BracketType.ROUND -> 1
        BracketType.SQUARE -> 2
        BracketType.CURLY -> 3
        BracketType.ANGLE -> 4
        BracketType.EMPTY -> 0
    }
}

private fun part2(input: List<String>): Long {
    val sums : MutableList<Long> = mutableListOf()

    input.forEach { line ->
        var isValid = true

        val brackets = line.trim().chunked(1).map { it[0] }

        val readOpened: MutableList<BracketType> = mutableListOf()

        for (i in brackets.indices) {
            val type = identifyBracketType(brackets[i])

            when(isBracketOpening(brackets[i])) {
                true -> readOpened.add(type)
                false -> {
                    if (readOpened.isNotEmpty() && readOpened.last() == type) {
                        readOpened.removeLast()
                    } else {
                        isValid = false
                        break
                    }
                }
            }
        }

        if (isValid) {
            var current = 0L

            for (i in (0 until readOpened.size).reversed()) {
                current = (current*5) + getValueFor2(readOpened[i]).toLong()
            }

            sums.add(current)
        }
    }

    sums.sort()
    return sums[sums.size / 2]
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957L)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}