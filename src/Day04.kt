fun main() {
    fun toNumbers(input: String) =
        input
            .split(",")
            .flatMap {
                it
                    .split("-")
                    .map { el -> el.toInt() }
            }

    fun isFullyContained(a: Int, b: Int, c: Int, d: Int) =
        a >= c && b <= d

    fun isPartiallyContained(a: Int, b: Int, c: Int, d: Int) =
        a <= d && b >= c

    fun part1(input: List<String>) =
        input.count {
            val num = toNumbers(it)
            (isFullyContained(num[0], num[1], num[2], num[3])
                    || isFullyContained(num[2], num[3], num[0], num[1]))
        }

    fun part2(input: List<String>) =
        input.count {
            val num = toNumbers(it)
            (isPartiallyContained(num[0], num[1], num[2], num[3]))
                    || isPartiallyContained(num[2], num[3], num[0], num[1])
        }

    val input = readInputAsList("Day04")
    println(part1(input))
    println(part2(input))

    // alternative solution
    fun getRanges(num: List<Int>) =
        listOf(
            num[0] in num[2]..num[3],
            num[1] in num[2]..num[3],
            num[2] in num[0]..num[1],
            num[3] in num[0]..num[1]
        )

    fun part1Alt(input: List<String>) =
        input.count {
            val (a, b, c, d) = getRanges(toNumbers(it))
            (a && b) || (c && d)
        }

    fun part2Alt(input: List<String>) =
        input.count {
            val (a, b, c, d) = getRanges(toNumbers(it))
            a || b || c || d
        }

    //println(part1Alt(input))
    //println(part2Alt(input))
}
