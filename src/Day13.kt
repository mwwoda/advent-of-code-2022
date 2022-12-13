// no discriminated unions in Kotlin AFAIK. big sad ;__;
data class Packet(val intValue: Int, val listValue: List<Packet> = listOf()) {
    constructor(listValue: List<Packet>) : this(-1, listValue)

    fun isList(): Boolean =
        intValue == -1
}

fun compareValues(left: Int, right: Int): Int {
    return if (left < right) {
        1
    } else if (left > right) {
        -1
    } else {
        0
    }
}

fun compareLists(left: List<Packet>, right: List<Packet>): Int {
    left
        .zip(right)
        .forEach {
            val res = comparePackets(it.first, it.second)
            if (res != 0) {
                return res
            }
        }
    return if (left.count() < right.count()) {
        1
    } else if (left.count() > right.count()) {
        -1
    } else {
        0
    }
}

fun comparePackets(left: Packet, right: Packet): Int {
    return when {
        !left.isList() && !right.isList() -> {
            compareValues(left.intValue, right.intValue)
        }
        !left.isList() && right.isList() -> {
            compareLists(listOf(left), right.listValue)
        }
        left.isList() && !right.isList() -> {
            compareLists(left.listValue, listOf(right))
        }
        left.isList() && right.isList() -> {
            compareLists(left.listValue, right.listValue)
        }
        else -> throw Exception("Unexpected comparison!")
    }

}

val packetComparator = Comparator<Packet> { a, b -> comparePackets(a, b) }

fun main() {
    fun parsePackets(string: String, index: Int = 0): Pair<Packet, Int> {
        val packetList = mutableListOf<Packet>()
        var currentIndex = index
        while (currentIndex < string.count()) {
            when {
                string[currentIndex] == ']' -> {
                    return Pair(Packet(packetList), currentIndex)
                }
                string[currentIndex].isDigit() -> {
                    if (currentIndex + 1 < string.count() && string[currentIndex + 1].isDigit()) {
                        val value = string.substring(currentIndex, currentIndex + 2).toInt()
                        packetList.add(Packet(value))
                        currentIndex++
                    }
                    val value = string[currentIndex].digitToInt()
                    packetList.add(Packet(value))
                }
                string[currentIndex] == '[' -> {
                    val currList = parsePackets(string, currentIndex + 1)
                    currentIndex = currList.second
                    packetList.add(currList.first)
                }
            }
            currentIndex += 1
        }
        return Pair(packetList[0], -1)
    }

    fun getPacketList(input: List<String>) =
        input
            .windowed(2, 3)
            .flatMap {
                it.map { s -> parsePackets(s).first }
            }

    fun countOrderedPackets(packetList: List<Packet>) =
        packetList
            .asSequence()
            .chunked(2)
            .withIndex()
            .filter { p -> comparePackets(p.value[0], p.value[1]) == 1 }
            .map { p -> p.index + 1 }
            .sum()

    fun locateDecoderKey(packetList: List<Packet>): Int {
        val firstDividerPacket = Packet(listOf(Packet(2)))
        val secondDividerPacket = Packet(listOf(Packet(6)))
        val newPacketList = packetList + firstDividerPacket + secondDividerPacket

        val sortedPackets = newPacketList.sortedWith(packetComparator.reversed())
        val indexOf1 = sortedPackets.indexOf(firstDividerPacket)
        val indexOf2 = sortedPackets.indexOf(secondDividerPacket)
        return ((indexOf1 + 1) * (indexOf2 + 1))
    }

    fun part1(input: List<String>) =
        countOrderedPackets(getPacketList(input))

    fun part2(input: List<String>) =
        locateDecoderKey(getPacketList(input))

    val input = readInputAsList("Day13")
    println(part1(input))
    println(part2(input))
}
