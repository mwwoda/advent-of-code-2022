fun main() {
    data class Command(val value: Int, val cycle: Int)

    val emptyCommand = Command(-1, -1)

    fun isInSprite(register: Int, pixel: Int) =
        pixel - register in 0..2

    fun printScreen(rows: List<List<Boolean>>) =
        rows.forEach {
            it.forEach { el ->
                if (el) print('#') else print('.')
            }
            println()
        }

    fun getCommand(
        input: List<String>,
        inputPointer: Int,
        currentCycle: Int
    ): Command {
        val commands = input[inputPointer].split(' ')
        return if (commands[0] == "addx") {
            Command(commands[1].toInt(), currentCycle + 1)
        } else emptyCommand
    }

    fun shouldGetCommand(
        inputPointer: Int,
        input: List<String>,
        commandToExecute: Command
    ) = inputPointer < input.size && commandToExecute == emptyCommand

    fun part1(input: List<String>): Int {
        var commandToExecute = emptyCommand
        var register = 1
        var inputPointer = 0

        val cycles = 220

        val res = (0 until cycles).fold(listOf<Int>()) { acc, i ->
            val currentCycle = i + 1

            if (shouldGetCommand(inputPointer, input, commandToExecute)) {
                commandToExecute = getCommand(input, inputPointer, currentCycle)
                inputPointer++
            }

            val signal = if ((currentCycle - 20) % 40 == 0) register * currentCycle else 0

            if (commandToExecute.cycle == currentCycle) {
                register += commandToExecute.value
                commandToExecute = emptyCommand
            }
            acc + signal
        }

        return res.sum()

    }

    fun part2(input: List<String>): List<List<Boolean>> {
        var commandToExecute = emptyCommand
        var register = 1
        var inputPointer = 0

        val rows = 6
        val screenWidth = 40

        return (0 until rows).fold(listOf()) { acc, j ->
            val res = (0 until screenWidth).fold(listOf<Boolean>()) { intAcc, i ->
                val currentCycle = i + 1 + (j * screenWidth)

                if (shouldGetCommand(inputPointer, input, commandToExecute)) {
                    commandToExecute = getCommand(input, inputPointer, currentCycle)
                    inputPointer++
                }

                val pixelValue = isInSprite(register, i + 1)

                if (commandToExecute.cycle == currentCycle) {
                    register += commandToExecute.value
                    commandToExecute = emptyCommand
                }
                intAcc + pixelValue
            }
            acc + listOf(res)
        }
    }

    val input = readInputAsList("Day10")
    println(part1(input))
    printScreen(part2(input))
}
