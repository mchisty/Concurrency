package home.chisty.prioritytask;

import java.util.Random;
import java.util.stream.IntStream;

public class PriorityTaskDemo {
  private static final int NUMBER_OF_THREADS = 4;

  public static void main(String[] args) throws InterruptedException {
    PriorityTaskProcessor processor = new PriorityTaskProcessor(NUMBER_OF_THREADS);
    Random random = new Random();

    // Submit tasks with random priorities
    IntStream.range(0, 5)
        .forEach(
            i -> {
              int priority = random.nextInt(1, 11);
              processor.submitTask(new Task(i, priority, "Task " + i));
            });

    processor.startProcessing();

    // Let tasks process for a while
    Thread.sleep(500);

    processor.shutdown();

    // Print results
    processor.getResults().forEach(System.out::println);
  }
}
