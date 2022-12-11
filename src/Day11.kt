import java.util.*

fun main() {
    data class Test(val cond: Long, val trueMonkey: Int, val falseMonkey: Int) {}

    data class Operation(val operator: Char, val right: String) {}

    data class Monkey(val id: Int, val test: Test, val operation: Operation) {
        var currentItems: Queue<Long> = LinkedList()
        var timesInspected = 0
    }

    fun getOperation(operator: Char): (Long, Long) -> Long =
        when (operator) {
            '+' -> { a, b -> a + b }
            '*' -> { a, b -> a * b }
            else -> throw Exception("Unknown operator found!")
        }

    fun Operation.execute(oldValue: Long) =
        getOperation(operator).invoke(
            oldValue,
            if (right == "old") oldValue else right.toLong()
        )

    fun getCap(mods: List<Long>) = mods.reduce { acc, l -> acc * l }

    fun Test.getTargetMonkey(worryLevel: Long) =
        if (worryLevel % cond == 0L) trueMonkey else falseMonkey

    fun Monkey.inspect(divideBy: Int, cap: Long): Pair<Int, Long> {
        val currentItem = currentItems.poll()
        timesInspected++
        val currentWorryLevel = (operation.execute(currentItem) / divideBy) % cap
        val targetMonkey = test.getTargetMonkey(currentWorryLevel)
        return Pair(targetMonkey, currentWorryLevel)
    }

    fun dance(monkeys: List<Monkey>, numberOfRounds: Int, divideBy: Int): Long {
        val cap = getCap(monkeys.map { it.test.cond })
        repeat(numberOfRounds) {
            monkeys.forEach {
                while (it.currentItems.isNotEmpty()) {
                    val inspectionRes = it.inspect(divideBy, cap)
                    monkeys[inspectionRes.first].currentItems.add(inspectionRes.second)
                }
            }
        }
        return monkeys
            .asSequence()
            .map { it.timesInspected.toLong() }
            .sortedDescending()
            .take(2)
            .reduce { acc, i -> acc * i }
    }

    fun parseMonkeys(input: String) =
        input.split("\r\n\r\n").fold(listOf<Monkey>()) { acc, s ->
            val monkeyInput = s
                .lines()
                .map {
                    Regex("[0-9]+|old|\\+|\\*")
                        .findAll(it)
                        .map(MatchResult::value)
                        .toList()
                }
            val test = Test(
                monkeyInput[3][0].toLong(),
                monkeyInput[4][0].toInt(),
                monkeyInput[5][0].toInt()
            )
            val operation = Operation(
                monkeyInput[2][1][0],
                monkeyInput[2][2]
            )
            val startingItems = monkeyInput[1].map { it.toLong() }
            acc + Monkey(monkeyInput[0][0].toInt(), test, operation).also {
                it.currentItems = LinkedList(startingItems)
            }
        }

    fun part1(input: String) =
        dance(parseMonkeys(input), 20, 3)

    fun part2(input: String) =
        dance(parseMonkeys(input), 10000, 1)

    val input = readInputAsString("Day11")
    println(part1(input))
    println(part2(input))
}
