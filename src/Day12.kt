import java.util.*

private data class Cave(
    val name: String,
    val isSmall: Boolean
)

private fun getMapOfCaves(input: List<String>): Map<Cave, List<Cave>> {
    val mapOfCaves: MutableMap<Cave, MutableList<Cave>> = mutableMapOf()

    input.forEach {
        val nodes = it.trim().split("-")

        val first = Cave(nodes[0].lowercase(), nodes[0][0].isLowerCase())
        val second = Cave(nodes[1].lowercase(), nodes[1][0].isLowerCase())

        if (mapOfCaves.containsKey(first)) {
            val list = mapOfCaves[first]
            if ((list != null) && !list.contains(second)) {
                list.add(second)
            }
            mapOfCaves[first] = list!!
        } else {
            mapOfCaves[first] = mutableListOf(second)
        }

        if (mapOfCaves.containsKey(second)) {
            val list = mapOfCaves[second]
            if ((list != null) && !list.contains(first)) {
                list.add(first)
            }
            mapOfCaves[second] = list!!
        } else {
            mapOfCaves[second] = mutableListOf(first)
        }
    }

    return mapOfCaves
}

private fun checkValidity(current: List<Cave>, startCave: Cave, endCave: Cave, isLowerOnce: Boolean): Boolean {
    if (current.first() != startCave) return false

    if (isLowerOnce) {
        for (item in current.distinct()) {
            if (item.isSmall) {
                if (Collections.frequency(current, item) > 1) return false
            }
        }
    } else {
        var allowTwice = true
        for (item in current.distinct()) {
            if (item.isSmall) {
                val occurrence = Collections.frequency(current, item)
                if ((item == startCave || item == endCave) && occurrence > 1) return false
                if (allowTwice && occurrence == 2) {
                    allowTwice = false
                } else if (occurrence > 1) {
                    return false
                }
            }
        }
    }

    return true
}

private fun checkEndState(current: List<Cave>, startCave: Cave, endCave: Cave, isLowerOnce: Boolean): Boolean {
    if (!checkValidity(current, startCave, endCave, isLowerOnce)) return false
    if (current.last() == endCave) return true
    return false
}

private fun searchAllPath(map: Map<Cave, List<Cave>>, start: String, end: String, isLowerOnce: Boolean ): Int {
    val listOfPaths: MutableList<List<Cave>> = mutableListOf()

    val startCave = map.filter { it.key.name == start }.keys.first()
    val endCave = map.filter { it.key.name == end }.keys.first()

    val stateSpace: MutableList<List<Cave>> = mutableListOf()
    stateSpace.add(listOf(startCave))

    while (stateSpace.isNotEmpty()) {
        val current = stateSpace.removeFirst()
        when (checkValidity(current, startCave, endCave, isLowerOnce)) {
            false -> {} // do nothing, invalid
            true -> {
                when (checkEndState(current, startCave, endCave, isLowerOnce)) {
                    true -> {
                        listOfPaths.add(current)    // we have a solution, add to list
                    }
                    false -> {  // valid state but we are not at the end
                        map[current.last()]!!.forEach {
                            stateSpace.add(current.plus(it))
                        }
                    }
                }
            }
        }
    }
    return listOfPaths.size
}

private fun part1(input: List<String>): Int {
    val mapOfCaves = getMapOfCaves(input)
    return searchAllPath(mapOfCaves, "start", "end", true)
}

private fun part2(input: List<String>): Int {
    val mapOfCaves = getMapOfCaves(input)
    return searchAllPath(mapOfCaves, "start", "end", false)
}

fun main() {
    // test if implementation meets criteria from the description, like:
    var testInput = readInput("Day12_test_a")
    check(part1(testInput) == 10)
    check(part2(testInput) == 36)

    testInput = readInput("Day12_test_b")
    check(part1(testInput) == 19)
    check(part2(testInput) == 103)

    testInput = readInput("Day12_test_c")
    check(part1(testInput) == 226)
    check(part2(testInput) == 3509)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}