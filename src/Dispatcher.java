public abstract class Dispatcher {

    public Processor [] ArrayOfProcessors;
    int NUMBER_OF_DRAWS = 4;   // how many times before resigning Processor tries to
                               // giveAway*to/get*From (*task) other Processor
    public int systemTime = 0;

    public Dispatcher(Processor [] ArrayOfProcessors) {
        this.ArrayOfProcessors = ArrayOfProcessors;
    }

    public abstract void processTasks();

    public boolean isEveryProcessorFinished(){

        for (Processor processor : ArrayOfProcessors) {
            if (!processor.isFinished()) {
                return false;
            }
        }
        return true;
    }

    public Processor getRandomOtherProcessor(Processor processor){

        int randomIndex = (int) (Math.random() * ArrayOfProcessors.length);
        if(!ArrayOfProcessors[randomIndex].equals(processor)){return ArrayOfProcessors[randomIndex];}
        return getRandomOtherProcessor(processor);

    }

    // SYSTEM UPDATE ( co jedna jednostke czasu)

    // dla każdego procesora, dla każdego pierwszego elementu w kolejce (taska), zmniejsz executionTime o 1
    // jesli task.executionTime == 0, poll

    public void systemUpdate(){

        for(Processor processor : ArrayOfProcessors){

            if(!processor.currentTaskQueue.isEmpty()){
                processor.currentTaskQueue.peek().executionTime--;

                if(processor.currentTaskQueue.peek().executionTime == 0){
                    processor.currentMemoryUsage -= processor.currentTaskQueue.poll().taskSize;
                    // here's the definite execution of the task
                }
            }

            processor.totalMemoryUsage += processor.currentMemoryUsage;
//            System.out.println("[ System Time: " + systemTime + " ]\n");
//            System.out.println(processor.toStringCurrentStatus());
//            System.out.println();

            //uncomment this for visualisation
        }
        systemTime++;
    }

    public static double calculateStandardDeviation(double[] values) {
        if (values.length == 0) return 0.0;

        double sum = 0.0;
        for (double v : values) sum += v;
        double mean = sum / values.length;

        double squaredDiffSum = 0.0;
        for (double v : values) {
            squaredDiffSum += Math.pow(v - mean, 2);
        }

        return Math.sqrt(squaredDiffSum / values.length);
    }


    public void evaluateStatistics(Processor[] arrayOfProcessors, int numberOfRequests, int numberOfMigrations) {

        double[] averageMemoryUsages = new double[arrayOfProcessors.length];
        double sumOfAverageMemoryUsage = 0.0;

        for (int i = 0; i < arrayOfProcessors.length; i++) {
            Processor processor = arrayOfProcessors[i];
            double avgUsage = processor.totalMemoryUsage / systemTime;
            averageMemoryUsages[i] = avgUsage;
            sumOfAverageMemoryUsage += avgUsage;

            System.out.printf("Processor %d - AverageMemoryUsage: %.2f%n", processor.id, avgUsage);
        }

        double stdDev = calculateStandardDeviation(averageMemoryUsages);

        System.out.println("---------------------------");
        System.out.printf("Sum of AverageMemoryUsage: %.4f%n", sumOfAverageMemoryUsage);
        System.out.printf("Standard Deviation of AverageMemoryUsage: %.4f%n", stdDev);
        System.out.println("Number of requests: " + numberOfRequests);
        System.out.println("Number of migrations: " + numberOfMigrations);
        System.out.println("===========================\n");
    }

}
