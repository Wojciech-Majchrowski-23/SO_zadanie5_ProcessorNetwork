import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class Main {

    public static void main(String[] args) {

        int numberOfProcessors = 5;
        double MEMORY_SIZE_FOR_SINGLE_PROCESSOR = 1.00;
        double MEMORY_LOW_BAR_FOR_SINGLE_PROCESSOR = 0.20;
        double MEMORY_HIGH_BAR_FOR_SINGLE_PROCESSOR = 0.80;

        Processor [] ArrayProcessors = TaskQueueGenerator.createArrayOfProcessors(numberOfProcessors, MEMORY_SIZE_FOR_SINGLE_PROCESSOR, MEMORY_LOW_BAR_FOR_SINGLE_PROCESSOR, MEMORY_HIGH_BAR_FOR_SINGLE_PROCESSOR);

        Dispatcher dispatcher1 = new Dispatcher_LazyProcessors(deepCopy(ArrayProcessors));
        Dispatcher dispatcher2 = new Dispatcher_WorkaholicProcessors(deepCopy(ArrayProcessors));


        dispatcher1.processTasks();
        dispatcher2.processTasks();
    }

    public static Processor[] deepCopy(Processor[] original) {
        Processor[] copy = new Processor[original.length];

        for (int i = 0; i < original.length; i++) {
            Processor p = original[i];

            Queue<Task> localTaskQueueCopy = new PriorityQueue<>(Comparator.comparingInt(t -> t.arrivalTime));
            for (Task t : p.localTaskQueue) {
                localTaskQueueCopy.add(new Task(t.processorID, t.executionTime, t.arrivalTime, t.taskSize));
            }

            Processor processorCopy = new Processor(
                    p.id,
                    p.memorySize,
                    p.memoryLowBar,
                    p.memoryHighBar,
                    localTaskQueueCopy
            );

            if (p.currentTaskQueue != null) {
                processorCopy.currentTaskQueue = new PriorityQueue<>(Comparator.comparingInt(t -> t.arrivalTime));
                for (Task t : p.currentTaskQueue) {
                    processorCopy.currentTaskQueue.add(new Task(t.processorID, t.executionTime, t.arrivalTime, t.taskSize));
                }
            }

            processorCopy.currentMemoryUsage = p.currentMemoryUsage;
            processorCopy.totalMemoryUsage = p.totalMemoryUsage;

            copy[i] = processorCopy;
        }

        return copy;
    }
}

// ----- Simulation Assumptions -----

// n processors
// each processor has the same memory size and unique ID
// each processor has localTaskQueue with tasks that have its ID
// different localTaskQueues have the same size but randomly generated tasks
// each task has arrivalTime, executionTime and size

// everything is coordinated by Dispatcher
// its main goal is to process everyTask
// there are 3 types of dispatchers based on the strategy of how to distribute tasks to Processors
