import java.util.*

private fun part(input: List<String>, days: Int): Long {
    val maxValue = 8
    val initFishValues: List<Int> = input[0].split(",").map { Integer.parseInt(it.trim()) }

    val valuesCount: MutableMap<Int, Long> = mutableMapOf()

    for (i in 0 .. maxValue) {
        valuesCount[i] = Collections.frequency(initFishValues, i).toLong()
    }

    for (i in 0 until days) {
        // we need to store, how much we have on 0 - it will be used twice (add to 6 and put to 8)
        // others will be decremented by one / moved from index+1 to index
        val temp: Long = valuesCount[0]?:0L

        for (index in 0 .. maxValue) {
            when {
                index == 7 || index <= 5 -> valuesCount[index] = valuesCount[index + 1]?:0L
                index == 6 -> valuesCount[index] = (valuesCount[index + 1]?:0L) + temp
                else -> valuesCount[index] = temp // for 8
            }
        }
    }

    var sum = 0L
    valuesCount.forEach { sum += it.value }

    return sum
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part(testInput, 80) == 5934L)
    check(part(testInput, 256) == 26984457539L)

    val input = readInput("Day06")
    println(part(input, 80))
    println(part(input, 256))
}