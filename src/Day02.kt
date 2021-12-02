
fun main() {
    fun part1(input: List<String>): Int {
        var horizontal = 0
        var depth = 0

        input.forEach {
            val pair = it.split(" ")
            if (pair.size == 2) {
                val amount = Integer.parseInt(pair[1])
                when {
                    pair[0].equals("forward", true) -> horizontal+=amount
                    pair[0].equals("up", true) -> depth-=amount
                    pair[0].equals("down", true) -> depth+=amount
                }
            }
        }
        return depth*horizontal
    }

    fun part2(input: List<String>): Int {
        var horizontal = 0
        var depth = 0
        var aim = 0

        input.forEach {
            val pair = it.split(" ")
            if (pair.size == 2) {
                val amount = Integer.parseInt(pair[1])
                when {
                    pair[0].equals("forward", true) -> {
                        horizontal += amount
                        depth += aim*amount
                    }
                    pair[0].equals("up", true) -> aim-=amount
                    pair[0].equals("down", true) -> aim+=amount
                }
            }
        }
        return depth*horizontal
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
