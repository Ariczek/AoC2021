private data class PointDef(
    val x_cord: Int,
    val y_cord: Int
)

private data class LineDef(
    val startPoint: PointDef,
    val endPoint: PointDef
) {
    fun isHorizontalOrVertical(): Boolean {
        return (startPoint.x_cord == endPoint.x_cord) || (startPoint.y_cord == endPoint.y_cord)
    }

    private fun getNumOfPointsBetween(): Int {
        return when (startPoint.x_cord) {
            endPoint.x_cord -> when {
                    startPoint.y_cord < endPoint.y_cord -> {
                        endPoint.y_cord - startPoint.y_cord - 1
                    }
                    else -> startPoint.y_cord - endPoint.y_cord - 1
                }
            else -> when {
                    startPoint.x_cord < endPoint.x_cord -> {
                        endPoint.x_cord - startPoint.x_cord - 1
                    }
                    else -> startPoint.x_cord - endPoint.x_cord - 1
                }
        }
    }

    fun toPoints(): List<PointDef> {
        val list: MutableList<PointDef> = mutableListOf()
        val numOfPointsBetween = getNumOfPointsBetween()

        list.add(startPoint)

        val stepX = when {
            (startPoint.x_cord > endPoint.x_cord) -> -1
            (startPoint.x_cord == endPoint.x_cord) -> 0
            else -> 1
        }

        val stepY = when {
            (startPoint.y_cord > endPoint.y_cord) -> -1
            (startPoint.y_cord == endPoint.y_cord) -> 0
            else -> 1
        }

        for (i in 1 .. numOfPointsBetween) {
            list.add(PointDef(
                x_cord = startPoint.x_cord + stepX*i,
                y_cord = startPoint.y_cord + stepY*i
            ))
        }

        list.add(endPoint)

        return list
    }
}

private fun parseLine(input: String): LineDef {
    val points = input.split("->")
    val pointA = points[0].split(",")
    val pointB = points[1].split(",")

    return LineDef(
        startPoint = PointDef(
            x_cord = Integer.parseInt(pointA[0].trim()),
            y_cord = Integer.parseInt(pointA[1].trim())
        ),
        endPoint = PointDef(
            x_cord = Integer.parseInt(pointB[0].trim()),
            y_cord = Integer.parseInt(pointB[1].trim())
        )
    )
}


private fun part1(input: List<String>): Int {
    val listOfLines: List<LineDef> = input.map { parseLine(it) }
    val listOfHorOrVerLines = listOfLines.filter { it.isHorizontalOrVertical() }
    val mapOfPoints: MutableMap<PointDef, Int> = mutableMapOf()

    listOfHorOrVerLines.forEach { line ->
        val pointsInLine = line.toPoints()

        pointsInLine.forEach { point ->
            if (mapOfPoints.containsKey(point)) {
                val current = mapOfPoints[point]
                mapOfPoints[point] = 1 + current!!
            } else {
                mapOfPoints[point] = 1
            }
        }
    }

    return mapOfPoints.filter { entry ->
        entry.value >= 2
    }.size
}

private fun part2(input: List<String>): Int {
    val listOfLines: List<LineDef> = input.map { parseLine(it) }
    val mapOfPoints: MutableMap<PointDef, Int> = mutableMapOf()

    listOfLines.forEach { line ->
        val pointsInLine = line.toPoints()

        pointsInLine.forEach { point ->
            if (mapOfPoints.containsKey(point)) {
                val current = mapOfPoints[point]
                mapOfPoints[point] = 1 + current!!
            } else {
                mapOfPoints[point] = 1
            }
        }
    }

    return mapOfPoints.filter { entry ->
        entry.value >= 2
    }.size
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}