# Concurrency Pipeline

A Java project demonstrating a multi-threaded data processing pipeline using `CompletableFuture` and `BlockingQueue`.

## Project Structure

The project implements a 4-stage parallel data processing pipeline:
1. Stage 1: Generate random numbers
2. Stage 2: Filter even numbers
3. Stage 3: Calculate square
4. Stage 4: Aggregate results

## Features

- Multi-threaded processing using `CompletableFuture`
- Thread-safe communication using `BlockingQueue`
- Synchronized data collection
- Graceful shutdown mechanism

## How to Run

1. Clone the repository
2. Navigate to the project directory
3. Compile the code:
   ```bash
   javac src/main/java/home/chisty/pipeline/*.java
   ```
4. Run the main class:
   ```bash
   java -cp src/main/java home.chisty.pipeline.DataPipeline
   ```

## Dependencies

- Java 8 or higher 