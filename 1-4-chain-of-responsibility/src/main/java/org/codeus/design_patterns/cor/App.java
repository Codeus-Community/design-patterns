package org.codeus.design_patterns.cor;

public class App {
    public static void main(String[] args) {
        BankRequestProcessor processor = new BankRequestProcessor();

        BankRequest transfer = new BankRequest("1", RequestType.TRANSFER, 1000, "user1");
        BankRequest bill = new BankRequest("2", RequestType.BILL_PAYMENT, 2000, "user2");
        BankRequest credit = new BankRequest("3", RequestType.CREDIT_APPLICATION, 150_000, "user3");
        BankRequest tooBig = new BankRequest("4", RequestType.TRANSFER, 250_000, "user4");

        System.out.println("=== Running demo ===");

        try {
            processor.process(transfer);
            System.out.println("After processing transfer: " + transfer);
        } catch (Exception e) {
            System.out.println("Transfer failed: " + e.getMessage());
        }

        try {
            processor.process(bill);
            System.out.println("After processing bill payment: " + bill);
        } catch (Exception e) {
            System.out.println("Bill payment failed: " + e.getMessage());
        }

        try {
            processor.process(credit);
            System.out.println("After processing credit application: " + credit);
        } catch (Exception e) {
            System.out.println("Credit application failed: " + e.getMessage());
        }

        try {
            processor.process(tooBig);
            System.out.println("After processing tooBig: " + tooBig);
        } catch (Exception e) {
            System.out.println("Too big transfer failed: " + e.getMessage());
        }

        // No unsupported type example here since type is an enum now

        System.out.println("=== Demo finished ===");
    }
}
