package org.example.solids;


interface CommunicationService{

    void makePhoneCall(String toNumber);
    void sendSms(String toNumber, String messageContent);
    void sendEmail(String receiverEmail, String emailContent);
}

interface PhoneService{
    void makePhoneCall(String toNumber);
}

interface SmsService{
    void sendSms(String toNumber, String messageContent);
}

interface EmailService{
    void sendEmail(String receiverEmail, String emailContent);

}


class GCPPhoneCallProvider implements PhoneService{

    @Override
    public void makePhoneCall(String toNumber) {
        System.out.println("Phone call from GCP");
    }
}


class TwilioProvider implements SmsService,EmailService{


    @Override
    public void sendSms(String toNumber, String messageContent) {
        System.out.println("Sending sms " + toNumber);

    }

    @Override
    public void sendEmail(String receiverEmail, String emailContent) {
        System.out.println("Sending email " + receiverEmail);

    }
}


class ProfileConnectService {

    private PhoneService phoneService;
    private EmailService emailService;
    private SmsService smsService;


    public ProfileConnectService(PhoneService phoneService, EmailService emailService, SmsService smsService) {
        this.phoneService = phoneService;
        this.emailService = emailService;
        this.smsService = smsService;
    }

    public void makeContact(String profileId){
        String email = "testEmaial";
        String phoneNumber= "1024";

        phoneService.makePhoneCall(phoneNumber);
        smsService.sendSms(phoneNumber, "Content");
        emailService.sendEmail(email, "Content");
    }
}


public class InterfaceSegregationDemo {
    public static void main(String[] args) {

    }
}
