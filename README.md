# File Sorter

A Kotlin application that generates and sorts large text files according to specific rules. The application consists of two main components:
1. A file generator that creates test files of a specified size
2. A file sorter designed to handle very large files (tested with files up to 1MB, designed for scalability to 100GB)

## Requirements

- JDK 21
- Kotlin 1.9.0 or higher
- Maven 3.6 or higher

## Building

```bash
mvn clean package
```

## Running

The application provides two commands: `generate` and `sort`.

### Generate a test file

```bash
java -jar target/file-sorter-1.0-SNAPSHOT-jar-with-dependencies.jar generate -s <size_in_bytes> -o <output_file>
```

Example:
- To generate a **1MB** file:
  ```bash
  java -jar target/file-sorter-1.0-SNAPSHOT-jar-with-dependencies.jar generate -s 1000000 -o test_1MB.txt
  ```

- To generate a **1GB** file:
  ```bash
  java -jar target/file-sorter-1.0-SNAPSHOT-jar-with-dependencies.jar generate -s 1000000000 -o test_1GB.txt
  ```


Arguments:
- `-s, --size`: Target file size in bytes (required)
- `-o, --output`: Output file path (required)

### Sort a file

```bash
java -jar target/file-sorter-1.0-SNAPSHOT-jar-with-dependencies.jar sort -i <input_file> -o <output_file>
```

Example:
```bash
java -jar target/file-sorter-1.0-SNAPSHOT-jar-with-dependencies.jar sort -i test.txt -o sorted.txt
```

Arguments:
- `-i, --input`: Input file path (required)
- `-o, --output`: Output file path (required)

## Implementation Details

### File Format
- Each line consists of a number followed by a period and a string
- Example: `123. Some text here`

### Constraints
- String part length ≤ 100 characters
- Number part ≤ 10^18
- File size can be very large (tested up to 100GB)

### Sorting Rules
1. Lines are sorted by the string part first
2. If string parts are equal, lines are sorted by the number part

### Implementation Approach

The sorter uses an external merge sort algorithm to handle large files efficiently:

1. **Splitting Phase**
   - Input file is read in chunks
   - Each chunk is sorted in memory
   - Sorted chunks are written to temporary files

2. **Merging Phase**
   - Uses a priority queue to merge multiple sorted chunks
   - Performs a k-way merge to produce the final sorted file
   - Memory efficient: only keeps one line from each chunk in memory

### Performance Considerations

- Uses buffered I/O for efficient file operations
- Implements external merge sort to handle files larger than available RAM
- Configurable chunk size to balance memory usage and performance
- Clean-up of temporary files after sorting

## Testing

Run the tests:
```bash
mvn test
```