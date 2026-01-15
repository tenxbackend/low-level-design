package org.example.solids;

enum PaymentType {
    CREDIT_CARD,
    UPI,
    NET_BANKING
}


interface PaymentProcessor{
    void processPayment(double amount);
}

class CreditCardProcessor implements PaymentProcessor{

    @Override
    public void processPayment(double amount) {
        System.out.println("Processing Credit Card payment of " + amount);
    }
}


class UPIProcessor implements PaymentProcessor{
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing UPI payment of " + amount);
    }
}


class NetBanking implements PaymentProcessor{

    @Override
    public void processPayment(double amount) {
        System.out.println("Processing Net banking payment of " + amount);

    }
}

class CryptoWallet implements PaymentProcessor{

    @Override
    public void processPayment(double amount) {
        System.out.println("Payment using Crypto Wallet");
    }
}

class CheckoutService {

    private PaymentProcessor paymentProcessor;

    public CheckoutService(PaymentProcessor processor){
        this.paymentProcessor = processor;
    }

    public void checkout(double amount) {
        this.paymentProcessor.processPayment(amount);
    }

}


public class OpenClosedPrincipleDemo {
    public static void main(String[] args) {

        PaymentProcessor upiProcessor = new CryptoWallet();
        CheckoutService checkoutService = new CheckoutService(upiProcessor);
        checkoutService.checkout(1000);
    }
}

class PaymentProcessorFactory{

    public static PaymentProcessor getProcessor(PaymentType paymentType){
        if (paymentType == PaymentType.UPI){
            return new UPIProcessor();
        }
        return new UPIProcessor();
    }
}