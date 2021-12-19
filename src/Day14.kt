private data class PairInsertionRule(
    val rule: String,
    val insert: String
)

private fun makeOneInsertionNaive(input: String, rules: List<PairInsertionRule>): String {
    val stringBuilder: StringBuilder = StringBuilder()

    for (i in 0 until input.length-1) {
        val currentDouble = input.subSequence(i, i+2)

        val rule = rules.firstOrNull { it.rule == currentDouble }

        stringBuilder.append(currentDouble[0])
        if (rule != null) {
            stringBuilder.append(rule.insert)
        }
    }
    stringBuilder.append(input.last())

    return stringBuilder.toString()
}

private fun prepareTable(input: String): MutableMap<String, Long> {
    val map: MutableMap<String, Long> = mutableMapOf()

    for (i in 0 until input.length-1) {
        val currentDouble = input.subSequence(i, i+2).toString()
        map[currentDouble] = (map[currentDouble]?.plus(1L))?:1L
    }

    return map
}

private fun makeOneInsertToMap(input: Map<String, Long>, rules: List<PairInsertionRule>): Map<String, Long> {
    val map: MutableMap<String, Long> = mutableMapOf()
    
    input.forEach { (pair, amount) ->
        val rule = rules.firstOrNull { it.rule == pair }

        if (rule == null) {
            map[pair] = (map[pair]?.plus(amount))?:amount
        } else {
            val firstSub = pair[0] + rule.insert
            val secondSub = rule.insert + pair[1]

            map[firstSub] = (map[firstSub]?.plus(amount))?:amount
            map[secondSub] = (map[secondSub]?.plus(amount))?:amount
        }
    }

    return map
}

private fun part1(input: List<String>): Int {
    var template = input[0]
    val listOfRules: MutableList<PairInsertionRule> = mutableListOf()

    input.subList(2,input.size).forEach {
        val pair = it.split(" -> ")
        listOfRules.add(PairInsertionRule(pair[0], pair[1]))
    }

    for (i in 1 .. 10) {
        template = makeOneInsertionNaive(template, listOfRules)
    }

    val frequencies = template.chunked(1).groupingBy { it.first() }.eachCount()

    return frequencies.maxOf { it.value } - frequencies.minOf { it.value }
}

private fun part2(input: List<String>): Long {
    val listOfRules: MutableList<PairInsertionRule> = mutableListOf()

    input.subList(2,input.size).forEach {
        val pair = it.split(" -> ")
        listOfRules.add(PairInsertionRule(pair[0], pair[1]))
    }

    var map: Map<String, Long> = prepareTable(input[0])

    for (i in 1 .. 40) {
        map = makeOneInsertToMap(map, listOfRules)
    }

    val characters: List<Char> = map.keys.map { it[0] }.distinct()
    val mapOfOccur: MutableMap<Char, Long> = mutableMapOf()

    characters.forEach { char ->
        val sum = map.keys.filter { key ->
            key.startsWith(char)
        }.sumOf { entry ->
            map[entry]?:0L
        }
        if (char == input[0].last()) {
            mapOfOccur[char] = sum + 1
        } else {
            mapOfOccur[char] = sum
        }
    }

    return mapOfOccur.values.maxOf { it } - mapOfOccur.values.minOf { it }

}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 1588)
    check(part2(testInput) == 2188189693529)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}