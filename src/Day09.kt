import kotlin.math.abs
import kotlin.math.sign

fun main() {
    data class Point(val x: Int, val y: Int)

    fun isNeighbor(head: Point, tail: Point) =
        abs(head.x - tail.x) <= 1 && abs(head.y - tail.y) <= 1

    fun sign(num: Int) = sign(num.toDouble()).toInt()

    fun getKnotPosition(head: Point, tail: Point) =
        tail.copy(
            x = tail.x + sign(head.x - tail.x),
            y = tail.y + sign(head.y - tail.y)
        )

    fun getPosAfterMove(direction: Char, point: Point) =
        when (direction) {
            'R' -> point.copy(x = point.x + 1)
            'L' -> point.copy(x = point.x - 1)
            'U' -> point.copy(y = point.y + 1)
            'D' -> point.copy(y = point.y - 1)
            else -> throw Exception("Not recognized direction: $direction")
        }

    fun part1(input: List<String>): Int {
        val startingPoint = Point(0, 0)
        var head = startingPoint
        var tail = startingPoint

        val visited = input.fold(setOf(startingPoint)) { acc, el ->
            val steps = el.substring(2, el.lastIndex + 1).toInt()

            acc + (0 until steps).fold(setOf()) { intAcc, _ ->
                head = getPosAfterMove(el[0], head)
                if (!isNeighbor(head, tail)) {
                    tail = getKnotPosition(head, tail)
                    intAcc + tail
                } else intAcc
            }
        }

        return visited.count()
    }

    fun part2(input: List<String>): Int {
        val startingPoint = Point(0, 0)
        val knots = MutableList(10) { startingPoint }
        knots[0] = startingPoint
        val visited =
            knots.mapIndexed { index: Int, p: Point -> index + 1 to mutableSetOf(p) }.toMap()

        input.forEach { el ->
            val steps = el.substring(2, el.lastIndex + 1).toInt()

            repeat(steps) {
                knots[0] = getPosAfterMove(el[0], knots[0])
                for (i in 0..8) {
                    if (!isNeighbor(knots[i], knots[i + 1])) {
                        knots[i + 1] =
                            getKnotPosition(knots[i], knots[i + 1])
                        visited[i + 1]!!.add(knots[i + 1])
                    }
                }
            }
        }

        return visited[9]!!.count()
    }

    val input = readInputAsList("Day09")
    println(part1(input))
    println(part2(input))
}
