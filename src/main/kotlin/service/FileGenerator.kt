package service

import model.LineEntry
import kotlin.random.Random

class FileGenerator(
    private val random: Random = Random.Default
) {
    fun generate(
        targetSizeBytes: Long,
        duplicateTextProbability: Double = 0.3
    ): Sequence<LineEntry> = sequence {
        var currentSize = 0L
        val existingTexts = mutableListOf<String>()

        while (currentSize < targetSizeBytes) {
            val entry = if (existingTexts.isNotEmpty() && random.nextDouble() < duplicateTextProbability) {
                generateEntryWithExistingText(existingTexts)
            } else {
                generateNewEntry().also { existingTexts.add(it.text) }
            }

            currentSize += entry.toString().length + 1 // +1 for newline
            yield(entry)
        }
    }

    private fun generateNewEntry(): LineEntry {
        val number = random.nextLong(1, 1e18.toLong())
        val textLength = random.nextInt(10, 100)
        val text = generateRandomText(textLength)
        return LineEntry(number, text)
    }

    private fun generateEntryWithExistingText(existingTexts: List<String>): LineEntry {
        val text = existingTexts[random.nextInt(existingTexts.size)]
        val number = random.nextLong(1, 1e18.toLong())
        return LineEntry(number, text)
    }

    private fun generateRandomText(length: Int): String {
        val words = listOf("apple", "banana", "cherry", "date", "elderberry", "fig", "grape")
        val result = StringBuilder()

        while (result.length < length) {
            if (result.isNotEmpty()) result.append(" ")
            result.append(words[random.nextInt(words.size)])
        }

        return result.toString().take(length)
    }
}