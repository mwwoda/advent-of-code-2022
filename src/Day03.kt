fun main() {
    fun getPriorities(
        charRange: CharRange,
        startingPriority: Int,
    ) =
        charRange.mapIndexed { index, c -> c to index + startingPriority }.toMap()

    val priorityMap = getPriorities('a'..'z', 1) + getPriorities('A'..'Z', 27)

    fun getDuplicatesPriorities(rucksack: String): Int {
        val compartments = rucksack
            .chunked(rucksack.length / 2)
            .map { it.toSet() }

        return compartments[0]
            .intersect(compartments[1])
            .sumOf { priorityMap[it]!! }
    }

    fun getBadgesPriorities(rucksacks: List<String>): Int {
        val compartments = rucksacks
            .map { it.toSet() }
            .sortedBy { it.size }

        return compartments[0]
            .intersect(compartments[1])
            .intersect(compartments[2])
            .sumOf { priorityMap[it]!! }
    }

    fun part1(input: List<String>) =
        input.sumOf {
            getDuplicatesPriorities(it)
        }

    fun part2(input: List<String>) =
        input
            .chunked(3)
            .fold(0) { total, el ->
                total + getBadgesPriorities(el)
            }

    val input = readInputAsList("Day03")
    println(part1(input))
    println(part2(input))
}
