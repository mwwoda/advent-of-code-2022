import kotlin.math.abs

// less fp than usual for performance sake
fun main() {
    fun calculateManhattan(a: Pair<Int, Int>, b: Pair<Int, Int>) =
        abs(a.first - b.first) + abs(a.second - b.second)

    val emptyPair = Pair(-1, -1)

    fun parse(input: List<String>) =
        input
            .map {
                Regex("-?[0-9]+")
                    .findAll(it)
                    .map(MatchResult::value)
                    .map { c -> c.toInt() }
                    .toList()
            }
            .map { Pair(Pair(it[0], it[1]), Pair(it[2], it[3])) }

    fun isReachable(manhattanDistances: List<Pair<Pair<Int, Int>, Int>>, x: Int, y: Int): Boolean {
        manhattanDistances.forEach {
            val distanceToTarget =
                calculateManhattan(Pair(x, y), Pair(it.first.first, it.first.second))
            if (it.second >= distanceToTarget) {
                return true
            }
        }
        return false
    }

    fun getPossibleBeacons(
        sensor: Pair<Int, Int>,
        manhattanDistance: Int,
        max: Int
    ): List<Pair<Int, Int>> {
        val possibleBeacons = mutableListOf<Pair<Int, Int>>()
        possibleBeacons.add(Pair(sensor.first - manhattanDistance - 1, sensor.second))
        possibleBeacons.add(Pair(sensor.first + manhattanDistance + 1, sensor.second))
        (0..manhattanDistance).forEach { i ->
            possibleBeacons.add(Pair(sensor.first + manhattanDistance - i, sensor.second + i + 1))
            possibleBeacons.add(Pair(sensor.first - manhattanDistance - i, sensor.second + i + 1))
            possibleBeacons.add(Pair(sensor.first + manhattanDistance - i, sensor.second - i - 1))
            possibleBeacons.add(Pair(sensor.first - manhattanDistance - i, sensor.second - i - 1))
        }

        return possibleBeacons.filter { it.first in 0..max && it.second in 0..max }
    }

    fun getBeaconPosition(
        manhattanDistances: List<Pair<Pair<Int, Int>, Int>>,
        possibleSensors: List<Pair<Int, Int>>
    ): Pair<Int, Int> {
        possibleSensors.forEach {
            val isReachable = isReachable(manhattanDistances, it.first, it.second)
            if (!isReachable) {
                return Pair(it.first, it.second)
            }
        }

        return emptyPair
    }

    fun part1(input: List<String>, targetY: Int): Int {
        val parsedInput = parse(input)

        val sensors =
            parsedInput.map { Pair(it.first, calculateManhattan(it.first, it.second)) }

        val cannotBe = mutableListOf<Pair<Int, Int>>()

        sensors.forEach {
            val distanceToTargetRow = abs(targetY - it.first.second)
            if (distanceToTargetRow <= it.second) {
                cannotBe.add(Pair(it.first.first, targetY))
                for (i in 1..(it.second - distanceToTargetRow)) {
                    cannotBe.add(Pair(it.first.first + i, targetY))
                    cannotBe.add(Pair(it.first.first - i, targetY))
                }
            }
        }

        val beaconsOnTargetRow =
            parsedInput
                .filter { it.second.second == targetY }
                .map { Pair(it.second.first, it.second.second) }

        return (cannotBe.toSet() - beaconsOnTargetRow.toSet()).count()
    }

    fun part2(input: List<String>, max: Int): Long {
        val parsedInput = parse(input)

        val sensors =
            parsedInput.map { Pair(it.first, calculateManhattan(it.first, it.second)) }

        sensors.forEach {
            val pos = getBeaconPosition(sensors - it, getPossibleBeacons(it.first, it.second, max))
            if (pos != emptyPair) {
                return pos.first * 4000000L + pos.second
            }
        }

        throw Exception("Empty pos for beacon not found")
    }

    val input = readInputAsList("Day15")
    println(part1(input, 2000000))
    println(part2(input, 4000000))
}
