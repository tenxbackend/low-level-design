package org.example.solids;


class EmailServiceProvider{

    public void sendEmail(String emailId, String content){
        System.out.println("Sending email to customer " + emailId);
    }
}

interface Notifier{
    void notify(String orderId);
}

class EmailNotifier implements Notifier{

    @Override
    public void notify(String orderId) {
        // order -> get the customer email and other details
        String email = "customerEmail";
        System.out.println("Sending email to customer " + email);
    }
}

class SmsNotifier implements Notifier{
    @Override
    public void notify(String orderId){
        System.out.println("Sending sms for orderId "+ orderId );
    }
}

class OrderProcessor{

    private Notifier notifier;

    public OrderProcessor(Notifier notifier){
        this.notifier = notifier;
    }

    public void processOrder(String orderId){

        // validating the order
        System.out.println("Validating the order");
        // updating the warehouse count/quantity
        System.out.println("Updating the warehouse");
        // notify the customer -> sendEmail
        notifier.notify(orderId);
    }
}



public class DependencyInversionDemo {
    public static void main(String[] args) {
        EmailNotifier emailNotifier = new EmailNotifier();
        SmsNotifier smsNotifier = new SmsNotifier();
        OrderProcessor orderProcessor = new OrderProcessor(smsNotifier);
        orderProcessor.processOrder("order123");
    }
}
