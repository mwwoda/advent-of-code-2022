fun main() {
    fun sumOf(input: String, sumOf: Int) =
        input.split("\r\n\r\n")
            .map { it.lines().sumOf { el -> el.toLong() } }
            .sortedDescending()
            .take(sumOf)
            .sum()

    fun part1(input: String) = sumOf(input, 1)

    fun part2(input: String) = sumOf(input, 3)

    val input = readInputAsString("Day01")
    println(part1(input))
    println(part2(input))
}
