public class Dispatcher_LazyProcessors extends Dispatcher{


    public Dispatcher_LazyProcessors(Processor [] ArrayOfProcessors) {
        super(ArrayOfProcessors);
    }

    @Override
    public void processTasks() {

        int numberOfRequests = 0;
        int numberOfMigrations = 0;

        while(!isEveryProcessorFinished()){
            for(Processor processor : ArrayOfProcessors){

                if(!processor.localTaskQueue.isEmpty()){ // a jezeli jest pusta, to nie bedzie sie juz robic system update

                    if(processor.localTaskQueue.peek().arrivalTime <= systemTime){

                        Task currentTask = processor.localTaskQueue.poll(); //uwazaj, bo sciagasz, wiec na pewno musi zostac przydzielone
                        boolean taskAssigned = false;

                        while(!taskAssigned){

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
                                    systemUpdate();

                                    numberOfMigrations++;
                                    //tu nastepuje migracja procesu

                                }else{
                                    currentNumberOfDraws++;
                                    systemUpdate();
                                }
                            }

                            if(!taskAssigned){
                                if(processor.currentMemoryUsage + currentTask.taskSize <= processor.memorySize
                                        && processor.currentMemoryUsage < processor.memoryHighBar){

                                    processor.currentTaskQueue.add(currentTask);
                                    processor.currentMemoryUsage += currentTask.taskSize;
                                    taskAssigned = true;
                                }
                            }
                            systemUpdate();
                        }
                    }else{
                        systemUpdate();
                    }
                }else if(!processor.currentTaskQueue.isEmpty()){
                    systemUpdate();
                }
            }
        }
        System.out.println("=== LazyProcessorsStatistics ===");
        evaluateStatistics(ArrayOfProcessors, numberOfRequests, numberOfMigrations);
    }
}
