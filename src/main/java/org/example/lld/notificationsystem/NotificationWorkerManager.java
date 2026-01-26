package org.example.lld.notificationsystem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotificationWorkerManager {

    private NotificationQueue queue;
    private NotificationSenderFactory factory;
    private int workers;
    private ExecutorService executorService;

    public NotificationWorkerManager(NotificationQueue queue, NotificationSenderFactory factory, int workers){
        this.queue = queue;
        this.factory = factory;
        this.workers = workers;
        this.executorService = Executors.newFixedThreadPool(workers);
    }

    public void start(){
        for(int i=0;i<workers;i++){
            NotificationWorker worker = new NotificationWorker(this.queue, this.factory);
            this.executorService.submit(worker);
        }

    }


}
