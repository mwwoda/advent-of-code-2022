import java.io.File

private fun read(name: String) = File("src", "$name.txt")

fun readInputAsList(name: String) = read(name).readLines()

fun readInputAsString(name: String) = read(name).readText()