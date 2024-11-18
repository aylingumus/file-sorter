package model

data class LineEntry(
    val number: Long,
    val text: String
) : Comparable<LineEntry> {
    companion object {
        fun parse(line: String): LineEntry {
            val parts = line.split(".", limit = 2)
            require(parts.size == 2) { "Invalid line format: $line" }

            val number = parts[0].trim().toLongOrNull()
                ?: throw IllegalArgumentException("Invalid number format: ${parts[0]}")

            require(number <= 1e18) { "Number exceeds maximum allowed value: $number" }

            val text = parts[1].trim()
            require(text.length <= 100) { "Text exceeds maximum length: ${text.length}" }

            return LineEntry(number, text)
        }
    }

    override fun compareTo(other: LineEntry): Int {
        return compareValuesBy(this, other,
            { it.text },
            { it.number }
        )
    }

    override fun toString(): String = "$number. $text"
}