package service

import kotlin.test.*
import kotlin.random.Random

class FileGeneratorTest {
    @Test
    fun `generates file of approximate size`() {
        val generator = FileGenerator(Random(42))
        val entries = generator.generate(1000).toList()

        val totalSize = entries.sumOf { it.toString().length + 1 }
        assertTrue(totalSize in 900..1100)
    }

    @Test
    fun `generates some duplicate texts`() {
        val generator = FileGenerator(Random(42))
        val entries = generator.generate(10000, duplicateTextProbability = 0.5).toList()

        val uniqueTexts = entries.map { it.text }.toSet()
        assertTrue(uniqueTexts.size < entries.size)
    }
}