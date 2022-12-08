fun main() {
    fun isOnEdge(x: Int, y: Int, columns: Int, rows: Int) =
        x == 0 || y == 0 || x == columns - 1 || y == rows - 1

    fun Int.left() = (this - 1 downTo 0)
    fun Int.right(max: Int) = (this + 1 until max)
    fun Int.up() = this.left()
    fun Int.down(max: Int) = this.right(max)

    fun <T> Iterable<T>.takeUntil(predicate: (T) -> Boolean): List<T> {
        val list = mutableListOf<T>()
        for (item in this) {
            list.add(item)
            if (predicate(item)) break
        }
        return list
    }

    fun yCond(
        matrix: Array<IntArray>, x: Int, value: Int
    ): (Int) -> Boolean = { matrix[it][x] >= value }

    fun xCond(
        matrix: Array<IntArray>, y: Int, value: Int
    ): (Int) -> Boolean = { matrix[y][it] >= value }

    fun isVisibleFrom(prog: IntProgression, cond: (Int) -> Boolean) =
        !prog.any(cond)

    fun isVisible(value: Int, x: Int, y: Int, matrix: Array<IntArray>) =
        isVisibleFrom(x.left(), xCond(matrix, y, value)) ||
                isVisibleFrom(x.right(matrix[x].size), xCond(matrix, y, value)) ||
                isVisibleFrom(y.up(), yCond(matrix, x, value)) ||
                isVisibleFrom(y.down(matrix.size), yCond(matrix, x, value))

    fun visibilityScoreFrom(
        prog: IntProgression,
        cond: (Int) -> Boolean
    ) = prog.takeUntil(cond).count()

    fun calculateScore(value: Int, x: Int, y: Int, matrix: Array<IntArray>) =
        visibilityScoreFrom(x.left(), xCond(matrix, y, value)) *
                visibilityScoreFrom(x.right(matrix[x].size), xCond(matrix, y, value)) *
                visibilityScoreFrom(y.up(), yCond(matrix, x, value)) *
                visibilityScoreFrom(y.down(matrix.size), yCond(matrix, x, value))

    fun createMatrix(input: List<String>) =
        input.map { el -> el.map { c -> c.digitToInt() }.toIntArray() }.toTypedArray()

    fun getVisibleTrees(grid: Array<IntArray>) =
        grid.foldIndexed(0) { y, acc, row ->
            acc + row.foldIndexed(0) { x, intAcc, el ->
                if (isVisible(el, x, y, grid)) intAcc + 1 else intAcc
            }
        }

    fun getHighestScore(grid: Array<IntArray>) =
        grid.mapIndexed { y, row ->
            row.mapIndexed { x, el ->
                if (!isOnEdge(x, y, grid.size, grid[0].size)) calculateScore(el, x, y, grid) else 0
            }.max()
        }.max()

    fun part1(input: List<String>) =
        getVisibleTrees(createMatrix(input))

    fun part2(input: List<String>) =
        getHighestScore(createMatrix(input))

    val input = readInputAsList("Day08")
    println(part1(input))
    println(part2(input))
}
