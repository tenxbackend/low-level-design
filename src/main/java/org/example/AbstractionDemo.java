package org.example;


class PaymentRequest {
    private String userId;
    private Double amount;
    private String mode;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}

class PaymentProcessor {

    public void processPayment(PaymentRequest request) {
        // validation logic
        PaymentValidator validator = new PaymentValidator(); // memory
        boolean isPaymentValid = validator.isPaymentValid(request);

        if (!isPaymentValid) throw new IllegalArgumentException("Invalid payment");

        // processing
        System.out.println("Payment being processed");
        System.out.println("Process completed");
    }
}

class PaymentValidator {
    public boolean isPaymentValid(PaymentRequest request) {
        if (request.getAmount().compareTo(Double.valueOf("2000")) > 0) {
            return false;
        }
        return true;
    }
}


public class AbstractionDemo {
    public static void main(String[] args) {

    }
}
