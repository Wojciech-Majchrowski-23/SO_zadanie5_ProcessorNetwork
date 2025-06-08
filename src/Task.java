public class Task {

    final int processorID;
    int executionTime;
    final int arrivalTime;
    final double taskSize; // (0.00 , 1.00)

    public Task(int processorID, int executionTime, int arrivalTime, double taskSize) {
        this.processorID = processorID;
        this.executionTime = executionTime;
        this.arrivalTime = arrivalTime;
        this.taskSize = taskSize;
    }

    @Override
    public String toString() {
        return String.format("Task[ processorID = %d, executionTime = %d, arrivalTime = %d, TASK_SIZE = %.2f ]",
                processorID, executionTime, arrivalTime, taskSize);
    }


}
