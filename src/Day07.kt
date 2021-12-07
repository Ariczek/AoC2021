import kotlin.math.absoluteValue

private fun part1(input: List<String>): Int {
    val list = input[0].split(",").map { Integer.parseInt(it.trim()) }

    val minValue: Int = list.minOrNull()?:0
    val maxValue: Int = list.maxOrNull()?:0

    var bestFuel = Integer.MAX_VALUE

    for (i in minValue .. maxValue) {
        var currentFuel = 0
        run compute@{
            list.forEach {
                currentFuel += (it - i).absoluteValue
                // we can skip computation if worse than best
                if (currentFuel > bestFuel) return@compute
            }
        }

        if (bestFuel >= currentFuel) {
            bestFuel = currentFuel
        }
    }

    return bestFuel
}

private fun part2(input: List<String>): Int {
    val list = input[0].split(",").map { Integer.parseInt(it.trim()) }

    val minValue: Int = list.minOrNull()?:0
    val maxValue: Int = list.maxOrNull()?:0

    var bestFuel = Integer.MAX_VALUE

    for (i in minValue .. maxValue) {
        var currentFuel = 0
        run compute@{
            list.forEach {
                val len = (it - i).absoluteValue
                if (len > 0) {
                    currentFuel += (len * (len + 1) / 2)
                    // we can skip computation if worse than best
                    if (currentFuel > bestFuel) return@compute
                }
            }
        }

        if (bestFuel >= currentFuel) {
            bestFuel = currentFuel
        }
    }

    return bestFuel
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}