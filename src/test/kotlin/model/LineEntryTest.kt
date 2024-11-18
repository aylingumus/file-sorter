package model

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LineEntryTest {
    @Test
    fun `parse valid line`() {
        val entry = LineEntry.parse("123. Test text")
        assertEquals(123L, entry.number)
        assertEquals("Test text", entry.text)
    }

    @Test
    fun `parse throws on invalid format`() {
        assertFailsWith<IllegalArgumentException> {
            LineEntry.parse("Invalid line")
        }
    }

    @Test
    fun `parse throws on invalid number`() {
        assertFailsWith<IllegalArgumentException> {
            LineEntry.parse("abc. Test")
        }
    }

    @Test
    fun `parse throws on number too large`() {
        assertFailsWith<IllegalArgumentException> {
            LineEntry.parse("1e19. Test")
        }
    }

    @Test
    fun `parse throws on text too long`() {
        assertFailsWith<IllegalArgumentException> {
            LineEntry.parse("1. ${"a".repeat(101)}")
        }
    }

    @Test
    fun `compare by text first`() {
        val entry1 = LineEntry(2, "Apple")
        val entry2 = LineEntry(1, "Banana")
        assertTrue(entry1 < entry2)
    }

    @Test
    fun `compare by number when texts are equal`() {
        val entry1 = LineEntry(2, "Apple")
        val entry2 = LineEntry(1, "Apple")
        assertTrue(entry1 > entry2)
    }
}
