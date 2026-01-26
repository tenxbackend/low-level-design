package org.example.lld.notificationsystem;

public class NotificationWorker implements Runnable {

    private NotificationQueue queue;
    private NotificationSenderFactory notificationSenderFactory;

    public NotificationWorker(NotificationQueue queue, NotificationSenderFactory notificationSenderFactory) {
        this.queue = queue;
        this.notificationSenderFactory = notificationSenderFactory;

    }


    @Override
    public void run() {
        System.out.println("Worker started running " + Thread.currentThread().getName());


        while (true) {
            try {
                Notification notification = this.queue.poll();
                System.out.println("Worker consumed message  " + Thread.currentThread().getName() + "  priority " + notification.priority.level);
                NotificationChannel channel = notification.channel;
                NotificationSender notificationSender = notificationSenderFactory.getSender(channel);

                notificationSender.send(notification);

            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }


        }

    }
}
