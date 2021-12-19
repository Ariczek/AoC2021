import java.util.*

private data class Position(
    val yCoordinate: Int,
    val xCoordinate: Int,
    val distance: Int
)

private fun dijkstra(riskLevels: List<List<Int>>): Int {
    val maxX = riskLevels[0].size
    val maxY = riskLevels.size

    // priority queue with distance as metric
    val queue = PriorityQueue(compareBy(Position::distance))

    // set all distances to infinity
    val distance = Array(maxY) { IntArray(maxX) { Int.MAX_VALUE } }

    // we did not visit any node at the moment
    val alreadyVisited = Array(maxY) { BooleanArray(maxX) }

    // operation relax - dest is from the previous point, from which we got here
    fun relax(yCoordinate: Int, xCoordinate: Int, dist: Int) {
        // check validity of given point, and that we did not visit it already
        if (yCoordinate !in 0 until maxY ||
            xCoordinate !in 0 until maxX ||
            alreadyVisited[yCoordinate][xCoordinate]) return

        // compute new destination
        val newDist = dist + riskLevels[yCoordinate][xCoordinate]

        // if we have better path, update distances and add this point with its new distance to queue
        if (newDist < distance[yCoordinate][xCoordinate]) {
            distance[yCoordinate][xCoordinate] = newDist
            queue += Position(yCoordinate, xCoordinate, newDist)
        }
    }

    // distance of start point is zero
    distance[0][0] = 0
    // add it to queue for processing
    queue.add(Position(0, 0, 0))

    // we need to proceed until we have visited the end point - it was top in the queue
    // there could be other points left in queue, but given there are not negative values of edge, they make it better
    while (!alreadyVisited[maxY - 1][maxX - 1]) {

        // get node from queue - means coordinates and the best distance from start
        val (yCoordinate, xCoordinate, dist) = queue.remove()

        if (alreadyVisited[yCoordinate][xCoordinate]) continue
        alreadyVisited[yCoordinate][xCoordinate] = true

        // relax all the adjanced nodes
        relax(yCoordinate - 1, xCoordinate, dist)
        relax(yCoordinate + 1, xCoordinate, dist)
        relax(yCoordinate, xCoordinate - 1, dist)
        relax(yCoordinate, xCoordinate + 1, dist)
    }

    // we know the distance, return
    return distance[maxY - 1][maxX - 1]
}

private fun part1(input: List<String>): Int {
    val riskLevels: MutableList<List<Int>> = mutableListOf()
    input.forEach { line ->
        riskLevels.add(line.chunked(1).map { risk -> Integer.parseInt(risk) })
    }

    return dijkstra(riskLevels)
}

private fun part2(input: List<String>): Int {
    val riskLevels: MutableList<List<Int>> = mutableListOf()
    // we are actually building 25 times bigger map, than in part1
    for (i in 0 .. 4) {
        input.forEach { line ->
            var part: List<Int> = listOf()
            for (j in 0..4) {
                part = part.plus(line.chunked(1).map { risk ->
                    ((Integer.parseInt(risk) + i + j - 1) % 9) + 1
                })
            }
            riskLevels.add(part)
        }
    }

    return dijkstra(riskLevels)
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 40)
    check(part2(testInput) == 315)

    val input = readInput("Day15")
    println(part1(input))
    println(part2(input))
}