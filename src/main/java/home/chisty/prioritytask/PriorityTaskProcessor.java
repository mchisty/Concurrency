package home.chisty.prioritytask;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Priority Task Processing with Thread Pool
 *
 * <p>### Problem Statement Create a system where multiple threads process tasks with different
 * priority levels. Tasks with higher priority (10) should be processed before lower priority tasks
 * (1). <br>
 * The system should demonstrate: <br/>
 * - Concurrent task submission <br/>
 * - Priority-based processing <br/>
 * - Thread pool usage <br/>
 * - Task result collection<br/>
 */
class PriorityTaskProcessor implements AutoCloseable {
    private final PriorityBlockingQueue<Task> taskPriorityBlockingQueue = new PriorityBlockingQueue<>();
    private final ExecutorService executorService;
    private final ConcurrentLinkedQueue<TaskResult> resultConcurrentLinkedQueue = new ConcurrentLinkedQueue<>();

    public PriorityTaskProcessor(int numThreads) {
        this.executorService = Executors.newFixedThreadPool(numThreads);
    }

    public void submitTask(Task task) {
        taskPriorityBlockingQueue.offer(task);
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
                Task task = taskPriorityBlockingQueue.poll(100, TimeUnit.MILLISECONDS);
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
                    String result = " --> Task %d (Priority: %d) processed by thread %s"
                            .formatted(
                                    task.id(),
                                    task.priority(),
                                    Thread.currentThread().getName());
                    resultConcurrentLinkedQueue.offer(new TaskResult(task.id(), result));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    @Override
    public void close() {
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

    public List<TaskResult> getResultConcurrentLinkedQueue() {
        return List.copyOf(resultConcurrentLinkedQueue);
    }
}
