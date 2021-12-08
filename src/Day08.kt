private fun part1(input: List<String>): Int {
    var counter = 0

    input.forEach { line ->
        val entry = line.trim().split("|")
        val digits = entry[1].trim().split(" ")

        digits.forEach { num ->
            when (num.length) {
                2, 3, 4, 7 -> counter ++
            }
        }
    }

    return counter
}

private fun containsAllCharsInAnyOrder(input: String, characters: String): Boolean {
    var contains = true
    characters.forEach { char ->
        if (!input.contains(char)) {
            contains = false
        }
    }
    return contains
}

private fun containsAllNotIgnoredCharsInAnyOrder(input: String, characters: String, ignored: String): Boolean {
    var contains = true
    characters.forEach { char ->
        if (!ignored.contains(char) && !input.contains(char)) {
            contains = false
        }
    }
    return contains
}

private fun translateDigitToNumber(digit: String, constCf: String, constBcdf: String): Int  = when (digit.length) {
    2 -> 1
    3 -> 7
    4 -> 4
    7 -> 8
    5 -> {
        // needs to check if contains "cf" signals from value 1 - any order
        if (containsAllCharsInAnyOrder(digit, constCf)) {
            3
            // we need to check for "bd" signals, as 4 - 1
        } else {
            if (containsAllNotIgnoredCharsInAnyOrder(digit, constBcdf, constCf)){
                5
            } else {
                2
            }
        }
    }
    6 -> {
        // if not "cf", it is 6
        if (!containsAllCharsInAnyOrder(digit, constCf)) {
            6
            // if it is "bcfd", it is 9
        } else {
            if (containsAllCharsInAnyOrder(digit, constBcdf)) {
                9
            } else {
                0
            }
        }
    }
    else -> 0
}

private fun part2(input: List<String>): Int {
    var counter = 0

    input.forEach { line ->

        val entry = line.trim().split("|")
        val signals = entry[0].trim().split(" ")
        val digits = entry[1].trim().split(" ")

        val map : MutableMap<Int, String> = mutableMapOf()

        map[1] = signals.find { it.length == 2 }!! // as "cf"
        map[4] = signals.find { it.length == 4 }!! // as "bcdf"

        var current = 0

        digits.forEach { digit ->
            val num = translateDigitToNumber(digit, map[1]!!, map[4]!!)

            current = (current * 10) + num
        }

        counter += current
    }

    return counter
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}