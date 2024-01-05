package model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine implements Runnable {
    private final List<Simulation> simulations;
    private final List<Thread> threads = new ArrayList<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);
    private int simNo = 0;

    public SimulationEngine(List<Simulation> simulations){
        this.simulations = simulations;
    }

    @Override
    public void run(){
        simulations.get(simNo).run();
    }

    public void runSync(){
        for (Simulation sim: simulations){
            sim.run();
        }
    }

    public void runAsync(){
        for (Simulation simulation : simulations) {
            Thread thread = new Thread(simulation);
            thread.start();
            threads.add(thread);
            simNo++;
        }
    }

    public void awaitSimulationsEnd() throws InterruptedException {
        for (Thread thread: threads){
            thread.join();
        }

        executorService.shutdown();
        if (!executorService.awaitTermination(10, TimeUnit.SECONDS)){
            executorService.shutdownNow();
        }
    }

    public void runAsyncInThreadPool(){
        for (Simulation simulation: simulations) {
            executorService.submit(simulation);
        }
    }
}
