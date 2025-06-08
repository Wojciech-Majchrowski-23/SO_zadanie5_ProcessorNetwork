public class Dispatcher_SamaritanProcessors extends Dispatcher{

    int numberOfRequests = 0;
    int numberOfMigrations = 0;

    public Dispatcher_SamaritanProcessors(Processor [] ArrayOfProcessors) {
        super(ArrayOfProcessors);
    }

    @Override
    public void processTasks() {

        while(!isEveryProcessorFinished()){
            // jeden cykl to przydzielenie po jednym tasku z kazdego localTaskQueue (kazdego processora)
            for(Processor processor : ArrayOfProcessors){

                if(!processor.localTaskQueue.isEmpty()){ // a jezeli jest pusta, to nie bedzie sie juz robic system update

                    if(processor.localTaskQueue.peek().arrivalTime <= systemTime){

                        Task currentTask = processor.localTaskQueue.poll(); //uwazaj, bo sciagasz, wiec na pewno musi zostac przydzielone
                        boolean taskAssigned = false;

                        while(!taskAssigned){

                            // !!! zmiana kolenosci wzgledem LazyProcessors !!!

                            if(processor.currentMemoryUsage + currentTask.taskSize <= processor.memorySize
                                    && processor.currentMemoryUsage < processor.memoryHighBar){
                                processor.currentTaskQueue.add(currentTask);
                                processor.currentMemoryUsage += currentTask.taskSize;
                                taskAssigned = true;
                            }

                            int currentNumberOfDraws = 0;

                            while(currentNumberOfDraws < NUMBER_OF_DRAWS && !taskAssigned){

                                Processor otherProcessor = getRandomOtherProcessor(processor);
                                numberOfRequests++;
                                //to nastepuje zapytanie sie innego procesu

                                if(otherProcessor.currentMemoryUsage + currentTask.taskSize <= processor.memorySize
                                        && otherProcessor.currentMemoryUsage < otherProcessor.memoryHighBar){

                                    otherProcessor.currentTaskQueue.add(currentTask);
                                    otherProcessor.currentMemoryUsage += currentTask.taskSize;
                                    taskAssigned = true;
                                    enhancedSystemUpdate();

                                    numberOfMigrations++;
                                    //tu nastepuje migracja procesu

                                }else{
                                    currentNumberOfDraws++;
                                    enhancedSystemUpdate();
                                }
                            }
                            enhancedSystemUpdate();
                        }
                    }else{
                        enhancedSystemUpdate();
                    }
                }else if(!processor.currentTaskQueue.isEmpty()){
                    enhancedSystemUpdate();
                }
            }
        }
        System.out.println("=== SamaritanProcessorsStatistics ===");
        evaluateStatistics(ArrayOfProcessors, numberOfRequests, numberOfMigrations);
    }

    public void enhancedSystemUpdate(){

        Processor helpingProcessor = null;

        for(Processor processor : ArrayOfProcessors){

            if(!processor.currentTaskQueue.isEmpty()){
                processor.currentTaskQueue.peek().executionTime--;

                if(processor.currentTaskQueue.peek().executionTime == 0){
                    processor.currentMemoryUsage -= processor.currentTaskQueue.poll().taskSize;

                    helpingProcessor = processor;
                    helpOtherProcessors(helpingProcessor);

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

    public void helpOtherProcessors(Processor helpingProcessor){

        if(helpingProcessor.currentMemoryUsage < helpingProcessor.memoryLowBar){

            Processor processorInNeed = getRandomOtherProcessorWithHighestCMU(helpingProcessor);
            // here is a request
            numberOfRequests++;

            // jesli jest jakis w potrzebie
            if(processorInNeed != null && !processorInNeed.currentTaskQueue.isEmpty()){
                // jesli pomagajacy Processor jest w stanie wziac na siebie taska
                if(helpingProcessor.currentMemoryUsage + processorInNeed.currentTaskQueue.peek().taskSize < helpingProcessor.memoryHighBar){

                    helpingProcessor.currentMemoryUsage += processorInNeed.currentTaskQueue.peek().taskSize;
                    helpingProcessor.currentTaskQueue.add(processorInNeed.currentTaskQueue.poll());

                    // here is migration
                    numberOfMigrations++;
                }
            }
        }
    }

    public Processor getRandomOtherProcessorWithHighestCMU(Processor processor){

        int index;
        Processor wantedProcessor = null;

        if(!ArrayOfProcessors[0].equals(processor)){
            index = 0;
        }else{
            index = 1;
        }
        wantedProcessor = ArrayOfProcessors[index];
        double highestCMU = ArrayOfProcessors[index].currentMemoryUsage;

        for(int i = index + 1; i < ArrayOfProcessors.length; i++){
            if(!ArrayOfProcessors[i].equals(processor) ){
                if(highestCMU < ArrayOfProcessors[i].currentMemoryUsage){
                    highestCMU = ArrayOfProcessors[i].currentMemoryUsage;
                    wantedProcessor = ArrayOfProcessors[i];
                }
            }
        }
        if(wantedProcessor.currentMemoryUsage < wantedProcessor.memoryHighBar){
            return null;
        }
        return wantedProcessor;
    }

}
