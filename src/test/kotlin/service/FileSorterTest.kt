package service

import java.io.File
import kotlin.io.path.createTempDirectory
import kotlin.test.*

class FileSorterTest {
    private lateinit var tempDir: File
    private lateinit var inputFile: File
    private lateinit var outputFile: File
    private lateinit var sorter: FileSorter

    @BeforeTest
    fun setUp() {
        tempDir = createTempDirectory("file-sorter-test").toFile()
        inputFile = File(tempDir, "input.txt")
        outputFile = File(tempDir, "output.txt")
        sorter = FileSorter(chunkSize = 2)
    }

    @AfterTest
    fun tearDown() {
        tempDir.deleteRecursively()
    }

    @Test
    fun `sorts file correctly`() {
        val input = listOf(
            "415. Apple",
            "30432. Something",
            "1. Apple",
            "32. Cherry",
            "2. Banana"
        )

        inputFile.writeText(input.joinToString("\n"))

        sorter.sort(inputFile, outputFile)

        val expected = listOf(
            "1. Apple",
            "415. Apple",
            "2. Banana",
            "32. Cherry",
            "30432. Something"
        )

        assertEquals(expected, outputFile.readLines())
    }

    @Test
    fun `handles empty file`() {
        inputFile.writeText("")
        sorter.sort(inputFile, outputFile)
        assertTrue(outputFile.readLines().isEmpty())
    }

    @Test
    fun `handles single line`() {
        inputFile.writeText("1. Test")
        sorter.sort(inputFile, outputFile)
        assertEquals(listOf("1. Test"), outputFile.readLines())
    }
}