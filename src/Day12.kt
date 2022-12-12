import kotlin.math.abs

fun main() {
    data class Node(val cords: Pair<Int, Int>)

    fun isWall(from: Char, to: Char) = to - from > 1

    fun createMatrix(input: List<String>) =
        input.map { el -> el.map { c -> c }.toCharArray() }.toTypedArray()

    fun getNodes(matrix: Array<CharArray>, value: Char) =
        matrix.asSequence()
            .flatMapIndexed { y, row ->
                row.withIndex()
                    .filter { c -> c.value == value }
                    .map { (x, _) -> Pair(y, x) }
            }

    fun getNode(matrix: Array<CharArray>, value: Char) =
        getNodes(matrix, value).first()

    fun getStartNode(matrix: Array<CharArray>) = getNode(matrix, 'S')

    fun getEndNode(matrix: Array<CharArray>) = getNode(matrix, 'E')

    fun calculateDistance(nodeA: Pair<Int, Int>, nodeB: Pair<Int, Int>) =
        abs(nodeA.first - nodeB.first) + abs(nodeA.second - nodeB.second)

    fun calculateH(endNode: Pair<Int, Int>, currentNode: Pair<Int, Int>) =
        calculateDistance(endNode, currentNode)

    fun getNeighbors(cords: Pair<Int, Int>, matrix: Array<CharArray>): Set<Node> {
        val upCords = if (cords.first - 1 >= 0) Node(cords.copy(first = cords.first - 1)) else null
        val downCords =
            if (cords.first + 1 <= matrix.size - 1) Node(cords.copy(first = cords.first + 1)) else null
        val leftCords =
            if (cords.second - 1 >= 0) Node(cords.copy(second = cords.second - 1)) else null
        val rightCords =
            if (cords.second + 1 <= matrix[0].size - 1) Node(cords.copy(second = cords.second + 1)) else null

        return setOfNotNull(upCords, downCords, leftCords, rightCords)
    }

    fun Array<CharArray>.markStartAndEnd(start: Pair<Int, Int>, end: Pair<Int, Int>) {
        this[start.first][start.second] = 'a'
        this[end.first][end.second] = 'z'
    }

    fun aStar(matrix: Array<CharArray>, start: Pair<Int, Int>, end: Pair<Int, Int>): Int {
        var currentNode = Node(start)
        val open = mutableSetOf(currentNode)
        val closed = mutableSetOf<Node>()

        val gMap = mutableMapOf(currentNode to 0)
        val fMap = mutableMapOf(currentNode to calculateH(end, start))

        while (open.isNotEmpty()) {
            currentNode = open.minBy { fMap[it]!! }

            if (currentNode.cords == end) {
                return fMap[currentNode]!!
            }

            open.remove(currentNode)
            closed.add(currentNode)

            val neighbors = ((getNeighbors(currentNode.cords, matrix)) - closed).filter {
                !isWall(
                    matrix[currentNode.cords.first][currentNode.cords.second],
                    matrix[it.cords.first][it.cords.second]
                )
            }
            neighbors.forEach {
                // jump cost is always 1
                val cost = gMap.getOrDefault(currentNode, Int.MAX_VALUE) + 1

                if (cost < gMap.getOrDefault(it, Int.MAX_VALUE)) {
                    gMap[it] = cost
                    fMap[it] = cost + calculateH(end, it.cords)
                    if (!open.contains(it)) {
                        open.add(it)
                    }
                }
            }

        }
        // path not found
        return Int.MAX_VALUE
    }

    fun part1(input: List<String>): Int {
        val matrix = createMatrix(input)
        val start = getStartNode(matrix)
        val end = getEndNode(matrix)
        matrix.markStartAndEnd(start, end)

        return aStar(matrix, start, end)
    }

    fun part2(input: List<String>): Int {
        val matrix = createMatrix(input)
        val start = getStartNode(matrix)
        val end = getEndNode(matrix)
        matrix.markStartAndEnd(start, end)

        // brute force approach
        val startPoints = getNodes(matrix, 'a').toList()
        return startPoints.minOf {
            aStar(matrix, it, end)
        }
    }

    val input = readInputAsList("Day12")
    println(part1(input))
    println(part2(input))
}
