private fun isLocalLowest(heightmap: MutableList<List<Int>>, yCord: Int, xCord: Int): Boolean {
    var isLocalLowest = true
    if (xCord != 0) {
        if (heightmap[yCord][xCord] >= heightmap[yCord][xCord-1]) {
            isLocalLowest = false
        }
    }
    if (xCord != heightmap[yCord].size-1) {
        if (heightmap[yCord][xCord] >= heightmap[yCord][xCord+1]) {
            isLocalLowest = false
        }
    }
    if (yCord != 0) {
        if (heightmap[yCord][xCord] >= heightmap[yCord-1][xCord]) {
            isLocalLowest = false
        }
    }
    if (yCord != heightmap.size-1) {
        if (heightmap[yCord][xCord] >= heightmap[yCord+1][xCord]) {
            isLocalLowest = false
        }
    }

    return isLocalLowest
}

private fun part1(input: List<String>): Int {
    val heightmap: MutableList<List<Int>> = mutableListOf()

    input.forEach { line->
        heightmap.add(line.chunked(1).map { char -> Integer.parseInt(char) })
    }

    var result = 0

    heightmap.forEachIndexed { yCord, line ->
        line.forEachIndexed { xCord, height ->
            if (isLocalLowest(heightmap, yCord, xCord)) {
                result += height + 1
            }
        }
    }

    return result
}

private data class PointInMap(
    val x_cord: Int,
    val y_cord: Int
) {
    private fun isInMap(xLimit: Int, yLimit: Int): Boolean {
        return (x_cord in 0..xLimit && y_cord in 0..yLimit)
    }

    fun generateAdjancedPointsWithLimit(xLimit: Int, yLimit: Int): List<PointInMap> {

        val points = listOf(PointInMap(x_cord-1, y_cord), PointInMap(x_cord+1, y_cord),
            PointInMap(x_cord, y_cord-1), PointInMap(x_cord, y_cord+1))

        return points.filter { point-> point.isInMap(xLimit, yLimit) }
    }
}

private fun computeSizeOfBasin(heightmap: MutableList<List<Int>>, yCord: Int, xCord: Int): Int {
    val startPoint = PointInMap(xCord, yCord)
    var currentSize = 1

    // basin includes the initial point
    val basinIncluded: MutableList<PointInMap> = mutableListOf()
    basinIncluded.add(startPoint)

    // search area contains up to 4 adjanced points (maybe less on edge of map)
    val searchArea: MutableList<PointInMap> = mutableListOf()
    searchArea.addAll(startPoint.generateAdjancedPointsWithLimit( heightmap[yCord].size-1, heightmap.size-1))

    while (searchArea.isNotEmpty()) {
        val currentPoint = searchArea.removeFirst()
        // height of 9 is not included in basin, just remove it
        if (heightmap[currentPoint.y_cord][currentPoint.x_cord] == 9) continue

        // lower height, basin included point, add to basin
        currentSize ++
        basinIncluded.add(currentPoint)

        // generate all the adjanced points
        val batch = currentPoint.generateAdjancedPointsWithLimit(heightmap[yCord].size-1, heightmap.size-1)

        // those not included in basin or search area already are added to search area
        searchArea.addAll(batch.filter { point -> !searchArea.contains(point) && !basinIncluded.contains(point) })
    }

    return currentSize
}

private fun part2(input: List<String>): Int {
    val heightmap: MutableList<List<Int>> = mutableListOf()

    input.forEach { line->
        heightmap.add(line.chunked(1).map { char -> Integer.parseInt(char) })
    }

    val list = mutableListOf<Int>()

    heightmap.forEachIndexed { yCord, line ->
        line.forEachIndexed { xCord, _ ->
            if (isLocalLowest(heightmap, yCord, xCord)) {
                list.add(computeSizeOfBasin(heightmap, yCord, xCord))
            }
        }
    }

    list.sortDescending()

    return list[0]*list[1]*list[2]
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}