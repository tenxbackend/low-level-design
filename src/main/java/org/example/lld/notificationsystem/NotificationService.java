package org.example.lld.notificationsystem;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

record NotificationRequest(
        String userId,
        NotificationChannel channel,
        NotificationPriority priority,
        Map<String, String> params
){}


enum NotificationChannel{
    SMS, EMAIL, PUSH, WHATSAPP;
}

enum NotificationPriority{
    HIGH(10), MEDIUM(5), LOW(1);
    final int level;
    NotificationPriority(int level){this.level = level;}
    public int getLevel(){return level;}
}


class Notification implements Comparable<Notification>{
    UUID id;
    String userId;
    NotificationChannel channel;
    NotificationPriority priority;
    Map<String, String> params;
    String status;
    LocalDateTime createdAt;
    LocalDateTime sentAt;

    @Override
    public int compareTo(Notification other) {

        int diff = Integer.compare(other.priority.getLevel(), this.priority.getLevel());
        if (diff == 0){
            return other.createdAt.compareTo(this.createdAt);
        }
        return diff;
    }
}


interface NotificationSender{
    void send(Notification notification);
    NotificationChannel getChannel();
}



class SmsSender implements NotificationSender{

    @Override
    public void send(Notification notification) {
        System.out.println("Sending SMS to " + notification.userId);
        System.out.println("text = " + notification.params.get("message"));
    }

    @Override
    public NotificationChannel getChannel() {
        return NotificationChannel.SMS;
    }
}


class EmailSender implements NotificationSender{

    @Override
    public void send(Notification notification) {
        System.out.println("Sending email to " + notification.params.get("emailId"));
        System.out.println("email content " +  notification.params.get("emailContent"));
    }

    @Override
    public NotificationChannel getChannel() {
        return NotificationChannel.EMAIL;
    }
}





class NotificationSenderFactory{

    private Map<NotificationChannel, NotificationSender> senderMap;

    public NotificationSenderFactory(List<NotificationSender> notificationSenderList){
        this.senderMap = new ConcurrentHashMap<>();

        notificationSenderList.forEach((sender) -> {
            this.senderMap.put(sender.getChannel(), sender);
        });
    }

    public NotificationSender getSender(NotificationChannel channel){
        return this.senderMap.get(channel);
    }

}



public class NotificationService {

    private NotificationSenderFactory notificationSenderFactory;
    private NotificationQueue notificationQueue;

    public NotificationService(NotificationSenderFactory notificationSenderFactory, NotificationQueue queue){
        this.notificationSenderFactory = notificationSenderFactory;
        this.notificationQueue = queue;

    }

    public void startWorkerThreads(){
        NotificationWorkerManager workerManager = new NotificationWorkerManager(this.notificationQueue, notificationSenderFactory,1);
        workerManager.start();
    }

    public void sendNotification(NotificationRequest notificationRequest){

        // validate the request
        if (notificationRequest.channel()==null || notificationRequest.userId() == null){
            throw new IllegalArgumentException("Invalid notification request");
        }
        Notification notification = createNotification(notificationRequest);
        System.out.println("notification created " + notification.id);
        System.out.println("notification saved to db");
        // enqueue -> add notification to the queue
        this.notificationQueue.push(notification);
        System.out.println("notification sent");
    }

    private static Notification createNotification(NotificationRequest notificationRequest) {
        Notification notification = new Notification();
        // use getters and setters , and make the fields of Notification private.
        // I have used public fields to avoid writing getters and setters.
        notification.channel = notificationRequest.channel();
        notification.userId = notificationRequest.userId();
        notification.priority = notificationRequest.priority();
        notification.params = notificationRequest.params();
        notification.createdAt = LocalDateTime.now();
        notification.id = UUID.randomUUID();

        return notification;
    }

}


class Demo{
    public static void main(String[] args) {
        NotificationSender smsSender = new SmsSender();
        NotificationSender emailSender = new EmailSender();


        NotificationSenderFactory senderFactory = new NotificationSenderFactory(List.of(smsSender, emailSender));
        NotificationQueue queue = new NotificationQueue(10);
        NotificationService notificationService = new NotificationService(senderFactory,queue);

        Map<String, String> params = new HashMap<>();
        params.put("message", "hello this is a test message");
        params.put("fromPhone", "1234");
        params.put("toPhone", "2345");
        params.put("emailId", "testEmailId");
        params.put("emailContent", " this is email content");

        NotificationRequest notificationRequest1 = new NotificationRequest(
                "user_id_1",
                NotificationChannel.SMS,
                NotificationPriority.HIGH,
                params
        );
        NotificationRequest notificationRequest2 = new NotificationRequest(
                "user_id_2",
                NotificationChannel.SMS,
                NotificationPriority.MEDIUM,
                params
        );
        NotificationRequest notificationRequest3 = new NotificationRequest(
                "user_id_3",
                NotificationChannel.SMS,
                NotificationPriority.LOW,
                params
        );

        notificationService.sendNotification(notificationRequest3);
        notificationService.sendNotification(notificationRequest2);
        notificationService.sendNotification(notificationRequest1);

        notificationService.startWorkerThreads();

    }
}
