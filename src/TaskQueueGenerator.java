import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class TaskQueueGenerator {

    public static int MAX_EXECUTION_TIME = 100;
    public static int MAX_ARRIVAL_TIME = 1000;
    public static int TASK_QUEUE_SIZE = 100;
    public static double TASK_SIZE = 0.7;

    /// returns taskQueue for single Processor (localTaskQueue)
    /// use it in Processor constructor

    public static Queue<Task> generateTaskQueueForProcessor(int processorId){

        Queue<Task> taskQueue = new PriorityQueue<Task>(Comparator.comparingInt(t -> t.arrivalTime));

        for(int i = 0; i < TASK_QUEUE_SIZE; i++){

            int executionTime = getRandomExecutionTime();
            int arrivalTime = getRandomArrivalTime();
            double taskSize = getRandomTaskSize();

            taskQueue.add(new Task(processorId, executionTime, arrivalTime, taskSize));
        }

        return taskQueue;
    }

    public static Processor [] createArrayOfProcessors(int numberOfProcessors, double MEMORY_SIZE_FOR_SINGLE_PROCESSOR, double MEMORY_LOW_BAR_FOR_SINGLE_PROCESSOR, double MEMORY_HIGH_BAR_FOR_SINGLE_PROCESSOR) {

        Processor [] ArrayOfProcessors = new Processor[numberOfProcessors];
        for (int i = 0; i < numberOfProcessors; i++) {
            ArrayOfProcessors[i] = new Processor(i,
                    MEMORY_SIZE_FOR_SINGLE_PROCESSOR,
                    MEMORY_LOW_BAR_FOR_SINGLE_PROCESSOR,
                    MEMORY_HIGH_BAR_FOR_SINGLE_PROCESSOR,
                    TaskQueueGenerator.generateTaskQueueForProcessor(i));
        }
        return ArrayOfProcessors;
    }

    public static int getRandomExecutionTime(){
        return (int)(Math.random() * MAX_EXECUTION_TIME) + 1;
    }

    public static int getRandomArrivalTime(){
        return (int)(Math.random() * MAX_ARRIVAL_TIME);
    }

    public static double getRandomTaskSize() {
        double raw = Math.random() * TASK_SIZE; // losuje z zakresu [0.0, 0.5)
        return Math.round(raw * 100.0) / 100.0 + 0.01; // zaokrąglenie do 2 miejsc po przecinku
    }

}
