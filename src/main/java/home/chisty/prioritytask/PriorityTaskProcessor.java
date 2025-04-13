package home.chisty.prioritytask;

import java.util.List;
import java.util.concurrent.*;

/**
 * Priority Task Processing with Thread Pool
 *
 * <p>### Problem Statement Create a system where multiple threads process tasks with different
 * priority levels. Tasks with higher priority (10) should be processed before lower priority tasks
 * (1). <br>
 * The system should demonstrate: - Concurrent task submission - Priority-based processing - Thread
 * pool usage - Task result collection
 */
class PriorityTaskProcessor {
  private final PriorityBlockingQueue<Task> taskQueue = new PriorityBlockingQueue<>();
  private final ExecutorService executorService;
  private final ConcurrentLinkedQueue<TaskResult> results = new ConcurrentLinkedQueue<>();

  public PriorityTaskProcessor(int numThreads) {
    this.executorService = Executors.newFixedThreadPool(numThreads);
  }

  public void submitTask(Task task) {
    taskQueue.offer(task);
  }

  public void startProcessing() {
    // Submit the task processor to each thread in the pool
    for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
      executorService.submit(() -> processTask());
    }
  }

  private void processTask() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        Task task = taskQueue.poll(100, TimeUnit.MILLISECONDS);
        if (task == null) {
          // If no task is available and executor is shutting down, exit
          if (executorService.isShutdown()) {
            break;
          }
          continue;
        }

        //                Simulate task processing with interruption checks.
        Thread.sleep(100);

        // Only record result if not interrupted
        if (!Thread.currentThread().isInterrupted()) {
          String result =
              " --> Task %d (Priority: %d) processed by thread %s"
                  .formatted(task.id(), task.priority(), Thread.currentThread().getName());
          results.offer(new TaskResult(task.id(), result));
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        break;
      }
    }
  }

  public void shutdown() {
    executorService.shutdown();
    try {
      // Wait for tasks to complete, but no longer than 5 seconds
      if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
        executorService.shutdownNow();
        System.out.println("Shut down processing threads");
      }
    } catch (InterruptedException e) {
      executorService.shutdownNow();
      Thread.currentThread().interrupt();
    }
  }

  public List<TaskResult> getResults() {
    return List.copyOf(results);
  }
}
