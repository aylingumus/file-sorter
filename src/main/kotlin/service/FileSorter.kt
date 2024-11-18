package service

import model.LineEntry
import java.io.BufferedReader
import java.io.File
import kotlin.io.path.createTempDirectory

class FileSorter(
    private val chunkSize: Int = 1_000_000, // Number of lines per chunk
    private val tempDir: File = createTempDirectory("sorter").toFile()
) {
    fun sort(input: File, output: File) {
        val chunks = splitIntoSortedChunks(input)
        mergeChunks(chunks, output)
        chunks.forEach { it.delete() }
        tempDir.delete()
    }

    private fun splitIntoSortedChunks(input: File): List<File> {
        val chunks = mutableListOf<File>()

        input.bufferedReader().use { reader ->
            var chunk = mutableListOf<LineEntry>()

            reader.lineSequence().forEach { line ->
                chunk.add(LineEntry.parse(line))

                if (chunk.size >= chunkSize) {
                    chunks.add(writeChunkToFile(chunk.sorted()))
                    chunk = mutableListOf()
                }
            }

            if (chunk.isNotEmpty()) {
                chunks.add(writeChunkToFile(chunk.sorted()))
            }
        }

        return chunks
    }

    private fun writeChunkToFile(entries: List<LineEntry>): File {
        val chunkFile = File(tempDir, "chunk-${System.nanoTime()}.txt")
        chunkFile.bufferedWriter().use { writer ->
            entries.forEach { writer.write("${it}\n") }
        }
        return chunkFile
    }

    private fun mergeChunks(chunks: List<File>, output: File) {
        val readers = chunks.map { it.bufferedReader() }
        val entries = readers.mapNotNull { readNextEntry(it) }.toMutableList()
        val pq = java.util.PriorityQueue<IndexedValue<LineEntry>>() { a, b ->
            a.value.compareTo(b.value)
        }

        entries.forEachIndexed { index, entry ->
            pq.offer(IndexedValue(index, entry))
        }

        output.bufferedWriter().use { writer ->
            while (pq.isNotEmpty()) {
                val (index, entry) = pq.poll()
                writer.write("${entry}\n")

                readNextEntry(readers[index])?.let {
                    pq.offer(IndexedValue(index, it))
                }
            }
        }

        readers.forEach { it.close() }
    }

    private fun readNextEntry(reader: BufferedReader): LineEntry? {
        return reader.readLine()?.let { LineEntry.parse(it) }
    }
}