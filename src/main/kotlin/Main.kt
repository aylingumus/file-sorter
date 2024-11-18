import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.file
import com.github.ajalt.clikt.parameters.types.long
import service.FileGenerator
import service.FileSorter

class GenerateCommand : CliktCommand(name = "generate") {
    private val size by option("-s", "--size", help = "Target file size in bytes")
        .long()
        .required()

    private val output by option("-o", "--output", help = "Output file")
        .file()
        .required()

    override fun run() {
        val generator = FileGenerator()
        output.bufferedWriter().use { writer ->
            generator.generate(size).forEach {
                writer.write("$it\n")
            }
        }
        echo("Generated file: ${output.absolutePath}")
    }
}

class SortCommand : CliktCommand(name = "sort") {
    private val input by option("-i", "--input", help = "Input file")
        .file()
        .required()

    private val output by option("-o", "--output", help = "Output file")
        .file()
        .required()

    override fun run() {
        val sorter = FileSorter()
        sorter.sort(input, output)
        echo("Sorted file: ${output.absolutePath}")
    }
}

fun main(args: Array<String>) {
    if (args.isEmpty() || args[0] !in setOf("generate", "sort")) {
        println("Usage: program <command> [options]")
        println("Commands:")
        println("  generate -s <size> -o <output>")
        println("  sort -i <input> -o <output>")
        return
    }

    when (args[0]) {
        "generate" -> GenerateCommand().main(args.drop(1))
        "sort" -> SortCommand().main(args.drop(1))
    }
}