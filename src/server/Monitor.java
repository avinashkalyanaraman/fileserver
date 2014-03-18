package server;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.TimerTask;

public class Monitor implements Runnable{

    public static long lastReqTime;
    public static final int idleTime = 60 * 60 * 1000 * 6; //6hr in ms
    
    public Monitor(long lastReqTime) {
        Monitor.lastReqTime = lastReqTime;
    }
    
    
    @Override
    public void run() {
        
        ScheduledExecutorService executor = Executors
                .newSingleThreadScheduledExecutor();

        Runnable task = new TimerTask() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                System.out.println(currentTime);
                if (currentTime > (lastReqTime + idleTime)) {
                    //committing harakiri. Parent may have died!
                    System.exit(0);
                }
            }
        };
        
        executor.scheduleAtFixedRate(task, 1, 10, TimeUnit.MINUTES);
        
    }

    public static void setLastReqTime(long currentTimeMillis) {
        if (currentTimeMillis > lastReqTime) {
            lastReqTime = currentTimeMillis;
        }
    }
    
    

}
