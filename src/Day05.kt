import java.util.*

fun main() {
    fun getStacksAndMoves(input: String): Pair<Map<Int, Stack<Char>>, List<List<Int>>> {
        val (stacksInput, movesInput) = input.split("\r\n\r\n")
        val stacks = stacksInput
            .lines()
            .fold(mutableMapOf<Int, Stack<Char>>()) { total, el ->
                el.mapIndexed { index, c ->
                    if (c.isLetter()) {
                        total
                            .getOrPut(((index - 1) / 4) + 1) { Stack<Char>() }
                            .push(c)
                    }
                }
                total
            }
            .onEach {
                it.value.reverse()
            }
            .toSortedMap()

        val moves = movesInput
            .lines()
            .map {
                Regex("[0-9]+")
                    .findAll(it)
                    .map(MatchResult::value)
                    .map { c -> c.toInt() }
                    .toList()
            }

        return Pair(stacks, moves)
    }

    fun stacksToString(stacks: Map<Int, Stack<Char>>) =
        stacks
            .map { it.value.pop() }
            .joinToString()
            .filter { it.isLetter() }

    fun part1(input: String): String {
        val (stacks, moves) = getStacksAndMoves(input)

        moves.forEach {
            repeat(it[0]) { _ -> stacks[it[2]]!!.push(stacks[it[1]]!!.pop()) }
        }

        return stacksToString(stacks)
    }

    fun part2(input: String): String {
        val (stacks, moves) = getStacksAndMoves(input)

        moves.forEach {
            val tempStack = Stack<Char>()

            repeat(it[0]) { _ -> tempStack.push(stacks[it[1]]!!.pop()) }
            repeat(it[0]) { _ -> stacks[it[2]]!!.push(tempStack.pop()) }
        }

        return stacksToString(stacks)
    }

    val input = readInputAsString("Day05")
    println(part1(input))
    println(part2(input))
}