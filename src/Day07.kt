fun main() {
    data class Node(
        val name: String,
        val isDir: Boolean,
        val parent: Node? = null,
        val size: Long = 0
    ) {
        val children: MutableList<Node> = mutableListOf()
    }

    // would be more optimal to set size once when the tree is created
    fun Node.getTotalSize(): Long =
        if (isDir) {
            children.sumOf { it.getTotalSize() }
        } else {
            size
        }

    fun createDirectoryTree(input: List<String>): List<Node> {
        var currentNode = Node("/", true)
        val directories = input
            .fold(listOf(currentNode)) { total, el ->
                val split = el.split(" ")
                when (split[0]) {
                    "dir" -> {
                        val node = Node(split[1], true, currentNode)
                        total + node.also {
                            currentNode.children.add(node)
                        }
                    }
                    "$" -> when (split[1]) {
                        "cd" -> {
                            total.also {
                                currentNode = when (split[2]) {
                                    ".." -> currentNode.parent!!
                                    "/" -> currentNode
                                    else -> currentNode.children.find { n -> n.name == split[2] }!!
                                }
                            }
                        }
                        else -> {
                            total
                        }
                    }
                    // digit
                    else -> {
                        total.also {
                            val node = Node(split[1], false, currentNode, split[0].toLong())
                            currentNode.children.add(node)
                        }
                    }
                }
            }
        return directories
    }

    fun part1(input: List<String>) =
        createDirectoryTree(input)
            .map { it.getTotalSize() }
            .filter { it < 100000 }
            .sum()

    fun part2(input: List<String>): Long {
        val tree = createDirectoryTree(input)
        val missingSpace = tree[0].getTotalSize() - (70000000 - 30000000)
        return tree.map { it.getTotalSize() }
            .filter { it >= missingSpace }
            .minBy { it }
    }

    val input = readInputAsList("Day07")
    println(part1(input))
    println(part2(input))
}