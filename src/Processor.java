import java.util.*;

public class Processor{

    public final int id;
    public final double memorySize;
    public final double memoryLowBar;
    public final double memoryHighBar;
    public final Queue<Task> localTaskQueue;

    public Queue<Task> currentTaskQueue;
    public double currentMemoryUsage;

    public double totalMemoryUsage;

    public Processor(int id, double memorySize, double memoryLowBar, double memoryHighBar, Queue<Task> localTaskQueue) {
        this.id = id;
        this.memorySize = memorySize;
        this.memoryLowBar = memoryLowBar;
        this.memoryHighBar = memoryHighBar;
        this.localTaskQueue = localTaskQueue;

        currentTaskQueue = new LinkedList<Task>();
        currentMemoryUsage = 0;
        totalMemoryUsage = 0;
    }

    public boolean isFinished(){
        return localTaskQueue.isEmpty() && currentTaskQueue.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Processor ").append(id).append(":\n");

        // Skopiuj kolejkę do listy i posortuj tymczasowo do wypisania
        List<Task> sortedTasks = new ArrayList<>(localTaskQueue);
        sortedTasks.sort(Comparator.comparingInt(t -> t.arrivalTime));

        for (Task task : sortedTasks) {
            sb.append(task).append("\n");
        }

        return sb.toString();
    }


    public String toStringCurrentStatus() {
        StringBuilder sb = new StringBuilder();

        // Dodajemy info o localTaskQueue (czy pusta)
        String localQueueStatus = (localTaskQueue == null || localTaskQueue.isEmpty()) ? "empty" : "not empty";
        sb.append("[ Processor").append(id)
                .append(" | LocalQueue: ").append(localQueueStatus).append(" ]\n");

        // Wypisanie currentTaskQueue
        sb.append("[ CurrentTaskQueue: ");
        if (currentTaskQueue == null || currentTaskQueue.isEmpty()) {
            sb.append("empty");
        } else {
            List<String> taskStrings = new ArrayList<>();
            for (Task task : currentTaskQueue) {
                taskStrings.add(task.toString());
            }
            sb.append(String.join(", ", taskStrings));
        }
        sb.append(" ]\n");

        // Wypisanie currentMemoryUsage
        sb.append(String.format("[ CurrentMemoryUsage: %.2f ]", currentMemoryUsage));

        return sb.toString();
    }




}
