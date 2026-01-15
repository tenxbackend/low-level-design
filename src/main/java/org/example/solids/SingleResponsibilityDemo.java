package org.example.solids;

import java.util.List;

record InvoiceItem(
        String itemId,
        String description,
        int quantity,
        double price
) { }

record Invoice(
        String invoiceId,
        String customerName,
        String customerEmail,
        List<InvoiceItem> items
) { }



class InvoiceService {

    private InvoiceAmountCalculator invoiceAmountCalculator = new InvoiceAmountCalculator();
    private InvoiceTemplateRenderer invoiceTemplateRenderer = new InvoiceTemplateRenderer();
    private InvoiceEmailSender invoiceEmailSender = new InvoiceEmailSender();

    public void generateInvoice(Invoice invoice) {

         //Calculate total amount
        double total = invoiceAmountCalculator.calculateTotalAmount(invoice);
        // Apply invoice templates
        String content = invoiceTemplateRenderer.renderTemplate(invoice.customerEmail(), total);
        // Deliver invoice
        System.out.println("Printing invoice:");
        System.out.println(content);
        invoiceEmailSender.sendInvoiceEmail(content, invoice.customerEmail());
    }
}

class InvoiceAmountCalculator{
    public double calculateTotalAmount(Invoice invoice){
        double total = 0;
        for (InvoiceItem item : invoice.items()) {
            total += item.price() * item.quantity();
        }
        return total;
    }
}

class InvoiceTemplateRenderer{

    public String renderTemplate(String customerEmail, Double totalAmount){
        String template = "Invoice for {{customer}} | Total: {{amount}}";
        String content = template
                .replace("{{customer}}", customerEmail)
                .replace("{{amount}}", String.valueOf(totalAmount));

        return content;
    }
}

class InvoiceEmailSender{

    public void sendInvoiceEmail(String content, String customerEmail){
        System.out.println("Email content "+ content);
        System.out.println("Emailing invoice to " +customerEmail);

    }
}


public class SingleResponsibilityDemo {
    public static void main(String[] args) {

    }
}
