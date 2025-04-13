package home.chisty.prioritytask;

public record Task(int id, int priority, String description) implements Comparable<Task> {
  @Override
  public int compareTo(Task other) {
    return Integer.compare(other.priority(), this.priority()); // Higher priority first
  }
}
