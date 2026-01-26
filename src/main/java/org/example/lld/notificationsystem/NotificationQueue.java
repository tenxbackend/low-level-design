package org.example.lld.notificationsystem;


import java.util.concurrent.PriorityBlockingQueue;

public class NotificationQueue {

    private PriorityBlockingQueue<Notification> queue;

    public NotificationQueue(int capacity){
        this.queue = new PriorityBlockingQueue<>(capacity);
    }

    public void push(Notification notification){
        System.out.println("added message to queue "+ notification);
        this.queue.offer(notification);
    }

    public Notification poll() throws InterruptedException {
        return this.queue.take();
    }

}
