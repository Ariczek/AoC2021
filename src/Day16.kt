private class MyPacket(input: String) {
    val version: String
    val typeId: String
    var operLengthTypeID: String = ""
    var literalValue: String = ""
    val fullLength: Int
    var subPacketsLength: Int = 0
    var numOfSubPackets: Int = 0
    val subPackets: MutableList<MyPacket> = mutableListOf()

    init {
        var position = 0
        version = input.substring(position, position + 3)
        position += 3
        typeId = input.substring(position, position + 3)
        position += 3

        if (isOperator()) {
            operLengthTypeID = input.substring(position, position + 1)
            position += 1
        }

        if (isOperator()) {
            when (operLengthTypeID[0]) {
                '0' -> {
                    // next 15 bits are subPackets length
                    subPacketsLength = Integer.parseInt(input.substring(position, position + 15), 2)
                    position += 15

                    var remaining = subPacketsLength
                    while (remaining > 0) {
                        val subPacket = MyPacket(input.substring(position, position + remaining))
                        subPackets.add(subPacket)
                        remaining -= subPacket.fullLength
                        position += subPacket.fullLength
                    }
                }
                '1' -> {
                    // next 11 bits are number of subPackets
                    numOfSubPackets = Integer.parseInt(input.substring(position, position + 11), 2)
                    position += 11

                    for (i in 0 until numOfSubPackets) {
                        val subPacket = MyPacket(input.substring(position))
                        subPackets.add(subPacket)
                        position += subPacket.fullLength
                    }
                }
            }
        } else {
            val number = StringBuilder()
            var cont = true
            input.substring(position).chunked(5).forEach { chunk ->
                if (!cont) return@forEach
                if (chunk.length == 5) {
                    number.append(chunk.substring(1, 5))
                    position += 5
                    cont = chunk[0] == '1'
                }
            }
            literalValue = number.toString()

        }
        fullLength = position
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("MyPacket: version:$version, typeId:$typeId, isOperator:${isOperator()}, fullLength=$fullLength, ")
        if (!isOperator()) sb.append("literal:${literalValue}")
        else {
            sb.append("contains: \n")
            sb.append(subPackets.joinToString(separator = "\n"))
        }

        return sb.toString()
    }

    private fun isOperator(): Boolean {
        return typeId != "100"
    }

    fun sumVersions(): Int {
        return Integer.parseInt(version, 2) + subPackets.sumOf { it.sumVersions() }
    }

    fun calculateValue(): Long {
        when (Integer.parseInt(typeId, 2)) {
            0 -> return subPackets.sumOf { it.calculateValue() }
            1 -> {
                var result = 1L
                subPackets.forEach { result *= it.calculateValue() }
                return result
            }
            2 -> return subPackets.minOf { it.calculateValue() }
            3 -> return subPackets.maxOf { it.calculateValue() }
            4 -> return literalValue.toLong(2)
            5 -> {
                val result = subPackets[0].calculateValue() > subPackets[1].calculateValue()
                return if (result) {
                    1
                } else {
                    0
                }
            }
            6 -> {
                val result = subPackets[0].calculateValue() < subPackets[1].calculateValue()
                return if (result) {
                    1
                } else {
                    0
                }
            }
            7 -> {
                val result = subPackets[0].calculateValue() == subPackets[1].calculateValue()
                return if (result) {
                    1
                } else {
                    0
                }
            }
            else -> return 0
        }
    }
}

private fun part1(input: String): Int {
    val inputBinary = StringBuilder()

    input.chunked(2).forEach {
        inputBinary.append(Integer.parseInt(it, 16).toString(2).padStart(8, '0'))
    }

    val myPacket = MyPacket(inputBinary.toString())

    println("Version sum: " + myPacket.sumVersions())

    return myPacket.sumVersions()
}

private fun part2(input: String): Long {
    val inputBinary = StringBuilder()

    input.chunked(2).forEach {
        inputBinary.append(Integer.parseInt(it, 16).toString(2).padStart(8, '0'))
    }

    val myPacket = MyPacket(inputBinary.toString())

    println("Calculated: " + myPacket.calculateValue())

    return myPacket.calculateValue()
}

fun main() {
    // test if implementation meets criteria from the description, like:
    var testInput = "D2FE28"
    check(part1(testInput) == 6)

    testInput = "8A004A801A8002F478"
    check(part1(testInput) == 16)

    testInput = "620080001611562C8802118E34"
    check(part1(testInput) == 12)

    testInput = "C0015000016115A2E0802F182340"
    check(part1(testInput) == 23)

    testInput = "A0016C880162017C3686B18A3D4780"
    check(part1(testInput) == 31)

    val input = readInput("Day16")
    println(part1(input[0]))

    testInput = "C200B40A82"
    check(part2(testInput) == 3L)

    testInput = "04005AC33890"
    check(part2(testInput) == 54L)

    testInput = "9C0141080250320F1802104A08"
    check(part2(testInput) == 1L)

    testInput = "F600BC2D8F"
    check(part2(testInput) == 0L)

    println(part2(input[0]))
}