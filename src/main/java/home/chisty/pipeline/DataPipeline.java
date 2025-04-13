package home.chisty.pipeline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/*
## Parallel Data Processing Pipeline

### Problem Statement
Create a multi-threaded pipeline that processes data through multiple stages:
1. Stage 1: Generate random numbers
2. Stage 2: Filter even numbers
3. Stage 3: Calculate square
4. Stage 4: Aggregate results

 */
class DataPipeline {
  private final BlockingQueue<Integer> allGeneratedNumbers = new LinkedBlockingQueue<>();
  private final BlockingQueue<Integer> filteredNumbers = new LinkedBlockingQueue<>();
  private final BlockingQueue<Integer> processedNumbers = new LinkedBlockingQueue<>();
  private final List<Integer> finalResults = Collections.synchronizedList(new ArrayList<>());
  private volatile boolean isRunning = true;

  public void startPipeline() {
    // Stage 1: Number Generator
    CompletableFuture.runAsync(this::generateNumbers);

    // Stage 2: Filter
    CompletableFuture.runAsync(this::filterNumbers);

    // Stage 3: Process
    CompletableFuture.runAsync(this::calculateSquare);

    // Stage 4: Aggregate
    CompletableFuture.runAsync(this::aggregateResults);
  }

  private void generateNumbers() {
    Random random = new Random();
    try {
      while (isRunning) {
        int number = random.nextInt(100);
        allGeneratedNumbers.put(number);
        Thread.sleep(100); // Simulate work
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  private void filterNumbers() {
    try {
      while (isRunning) {
        Integer number = allGeneratedNumbers.poll(100, TimeUnit.MILLISECONDS);
        if (number != null && number % 2 == 0) {
          filteredNumbers.put(number);
        }
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  private void calculateSquare() {
    try {
      while (isRunning) {
        Integer number = filteredNumbers.poll(100, TimeUnit.MILLISECONDS);
        if (number != null) {
            Integer squaredNumber = number * number;
            System.out.println("Selected number: " + number + ", squared number: " + squaredNumber);
          processedNumbers.put(squaredNumber);
        }
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  private void aggregateResults() {
    try {
      while (isRunning) {
        Integer number = processedNumbers.poll(100, TimeUnit.MILLISECONDS);
        if (number != null) {
          finalResults.add(number);
        }
      }

    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  public void shutdown() {
    isRunning = false;
  }

  public List<Integer> getResults() {
    return new ArrayList<>(finalResults);
  }
}
