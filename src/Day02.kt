enum class Symbol {
    Rock,
    Paper,
    Scissors
}

enum class Result {
    Win,
    Lose,
    Draw
}

val symbolMap = mapOf(
    'A' to Symbol.Rock,
    'B' to Symbol.Paper,
    'C' to Symbol.Scissors,
    'X' to Symbol.Rock,
    'Y' to Symbol.Paper,
    'Z' to Symbol.Scissors
)

val losingMap = mapOf(
    Symbol.Rock to Symbol.Paper,
    Symbol.Paper to Symbol.Scissors,
    Symbol.Scissors to Symbol.Rock
)

val winningMap = mapOf(
    Symbol.Rock to Symbol.Scissors,
    Symbol.Paper to Symbol.Rock,
    Symbol.Scissors to Symbol.Paper
)

val symbolScoreMap = mapOf(
    Symbol.Rock to 1,
    Symbol.Paper to 2,
    Symbol.Scissors to 3
)

val resultScoreMap = mapOf(
    Result.Win to 6,
    Result.Draw to 3,
    Result.Lose to 0
)

val requiredResultMap = mapOf(
    'X' to Result.Lose,
    'Y' to Result.Draw,
    'Z' to Result.Win
)

fun main() {
    fun getNeededSymbol(opp: Symbol, res: Result): Symbol =
        when (res) {
            Result.Win -> losingMap.getValue(opp)
            Result.Draw -> opp
            Result.Lose -> winningMap.getValue(opp)
        }

    fun calculateScoreBasedOnRequiredResult(opp: Symbol, res: Result): Int =
        resultScoreMap.getValue(res) + symbolScoreMap.getValue(getNeededSymbol(opp, res))

    fun calculateScore(player: Symbol, res: Result): Int =
        resultScoreMap.getValue(res) + symbolScoreMap.getValue(player)

    fun getResult(first: Symbol, second: Symbol): Result =
        when {
            second == winningMap.getValue(first) -> Result.Win
            first == second -> Result.Draw
            else -> Result.Lose
        }

    fun part1(input: List<String>): Int =
        input.sumOf {
            val opp = symbolMap.getValue(it[0])
            val player = symbolMap.getValue(it[2])
            val res = getResult(player, opp)
            calculateScore(player, res)
        }

    fun part2(input: List<String>): Int =
        input.sumOf {
            val opp = symbolMap.getValue(it[0])
            val reqResult = requiredResultMap.getValue(it[2])
            calculateScoreBasedOnRequiredResult(opp, reqResult)
        }

    val input = readInputAsList("Day02")
    println(part1(input))
    println(part2(input))
}

