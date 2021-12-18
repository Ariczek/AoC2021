private data class PointInfo(
    var x_coordinate: Int,
    var y_coordinate: Int
)

private data class FoldInfo(
    val type: Boolean,
    val lines: Int
)

private fun makeOneFold(points: List<PointInfo>, fold: FoldInfo): List<PointInfo> {
    val list: MutableList<PointInfo> = mutableListOf()

    points.forEach { point ->
        // x or y?
        if (fold.type) {
            if (point.x_coordinate > fold.lines) {
                point.x_coordinate = fold.lines - (point.x_coordinate - fold.lines)
            }
        } else {
            if (point.y_coordinate > fold.lines) {
                point.y_coordinate = fold.lines - (point.y_coordinate - fold.lines)
            }
        }

        if (!list.contains(point)) {
            list.add(point)
        }
    }

    return list
}

private fun part1(input: List<String>): Int {
    val listOfPoints: MutableList<PointInfo> = mutableListOf()
    val listOfFolds: MutableList<FoldInfo> = mutableListOf()

    input.forEach { line ->
        when {
            line.trim().isEmpty() -> {
                // just skip
            }
            line.trim().startsWith(prefix = "fold along") -> {
                val info = line.split(" ")[2].split("=")
                listOfFolds.add(FoldInfo(info[0]=="x", Integer.parseInt(info[1])))
            }
            else -> {
                val info = line.split(",")
                listOfPoints.add(PointInfo(Integer.parseInt(info[0]), Integer.parseInt(info[1])))
            }
        }
    }

    return makeOneFold(listOfPoints, listOfFolds.first()).size
}

private fun printDots(points: List<PointInfo>) {
    val maxX = points.maxOf { it.x_coordinate }
    val maxY = points.maxOf { it.y_coordinate }

    for (i in 0 .. maxY) {
        for (j in 0 .. maxX) {
            if (points.contains(PointInfo(j, i))) {
                print("#")
            } else {
                print(".")
            }
        }
        println()
    }
}

private fun part2(input: List<String>) {
    var listOfPoints: MutableList<PointInfo> = mutableListOf()
    val listOfFolds: MutableList<FoldInfo> = mutableListOf()

    input.forEach { line ->
        when {
            line.trim().isEmpty() -> {
                // just skip
            }
            line.trim().startsWith(prefix = "fold along") -> {
                val info = line.split(" ")[2].split("=")
                listOfFolds.add(FoldInfo(info[0]=="x", Integer.parseInt(info[1])))
            }
            else -> {
                val info = line.split(",")
                listOfPoints.add(PointInfo(Integer.parseInt(info[0]), Integer.parseInt(info[1])))
            }
        }
    }

    listOfFolds.forEach {
        listOfPoints = makeOneFold(listOfPoints, it).toMutableList()
    }

    printDots(listOfPoints)

}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 17)
//    check(part2(testInput) == 36)

    val input = readInput("Day13")
    println(part1(input))
    part2(input)
}