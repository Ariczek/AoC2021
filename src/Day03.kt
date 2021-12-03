
fun main() {
    fun part1(input: List<String>): Int {

        val numOfBits = input[0].length

        // list of occurrences for "1" and "0"
        val onesOccurrence: MutableList<Int> = MutableList(numOfBits) { 0 }
        val zerosOccurrence: MutableList<Int> = MutableList(numOfBits) { 0 }

        // for each input, count the character occurrence to proper position
        input.forEach {
            val line = it.chunked(1)
            line.forEachIndexed { index, s ->
                when (s) {
                    "0" -> {zerosOccurrence[index] += 1}
                    "1" -> {onesOccurrence[index] += 1}
                }
            }
        }

        val gamma : StringBuilder = StringBuilder()
        val epsilon : StringBuilder = StringBuilder()

        // for each position, give the most common char to gamma and least common to epsilon
        for (i in 0 until numOfBits) {
            if (onesOccurrence[i] > zerosOccurrence[i]) {
                gamma.append("1")
                epsilon.append("0")
            } else {
                gamma.append("0")
                epsilon.append("1")
            }
        }

        // compute and return the result
        val gammaRate = Integer.parseInt(gamma.toString(), 2)
        val epsilonRate = Integer.parseInt(epsilon.toString(), 2)

        return gammaRate * epsilonRate
    }

    fun part2(input: List<String>): Int {
        val numOfBits = input[0].length

        var onesOccurrence: Int
        var zerosOccurrence: Int

        var oxygen: List<String> = input
        var co2: List<String> = input

        // iterate over each character on a single line
        // the first iteration is doing it twice on the same set, oxygen == co2 == input
        for (i in 0 until numOfBits) {

            // while oxygen has more than one entry
            if (oxygen.size > 1) {
                onesOccurrence = 0
                zerosOccurrence = 0

                // count occurrences of bit on given position
                oxygen.forEach {
                    when (it.substring(i, i+1)) {
                        "0" -> {zerosOccurrence += 1}
                        "1" -> {onesOccurrence += 1}
                    }
                }

                // filter the set to only take most common bit entries
                // If the occurrences ties, we tak "1"
                oxygen = when (onesOccurrence >= zerosOccurrence) {
                    true -> oxygen.filter { it[i] == '1' }
                    false -> oxygen.filter { it[i] == '0' }
                }
            }

            // while co2 has more than one entry
            if (co2.size > 1) {
                onesOccurrence = 0
                zerosOccurrence = 0

                // count occurrences of bit on given position
                co2.forEach {
                    when (it.substring(i, i+1)) {
                        "0" -> {zerosOccurrence += 1}
                        "1" -> {onesOccurrence += 1}
                    }
                }

                // filter the set to only take the least common bit entries
                // If the occurrences ties, we tak "0"
                co2 = when (onesOccurrence >= zerosOccurrence) {
                    true -> co2.filter { it[i] == '0' }
                    false -> co2.filter { it[i] == '1' }
                }
            }
        }

        // presumption that we have a single entry in both sets here
        val oxygenRate = Integer.parseInt(oxygen[0], 2)
        val co2Rate = Integer.parseInt(co2[0], 2)

        // compute the result
        return oxygenRate * co2Rate
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
