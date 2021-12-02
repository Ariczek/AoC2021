fun main() {
    fun part1(input: List<String>): Int {
        val inputInt: List<Int> = input.map { Integer.parseInt(it) }
        var count = 0

        for (i in 0 until inputInt.size-1) {
            when (inputInt[i+1] > inputInt[i]) {
                true -> count ++
            }
        }

        return count
    }

    fun part2(input: List<String>): Int {
        val inputInt: List<Int> = input.map { Integer.parseInt(it) }
        var count = 0
        val slideWindowLen = 3

        for (i in 0 until inputInt.size - slideWindowLen) {

            val first = inputInt.subList(i, i+slideWindowLen).sum()
            val second = inputInt.subList(i+1, i+slideWindowLen+1).sum()

            when (second > first) {
                true -> count ++
            }
        }

        return count
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
