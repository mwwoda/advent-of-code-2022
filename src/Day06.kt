fun main() {
    fun getMessageMarker(input: String, markerLength: Int) =
        input.asSequence()
            .windowed(markerLength, 1)
            .mapIndexed { index, c -> index to c.toSet() }
            .first { it.second.count() == markerLength }
            .first + markerLength

    fun part1(input: String) = getMessageMarker(input, 4)

    fun part2(input: String) = getMessageMarker(input, 14)

    val input = readInputAsString("Day06")
    println(part1(input))
    println(part2(input))
}
