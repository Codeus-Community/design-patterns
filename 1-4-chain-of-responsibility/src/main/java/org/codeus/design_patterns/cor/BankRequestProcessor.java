package org.codeus.design_patterns.cor;

public class BankRequestProcessor {
    public void process(BankRequest request) {
        if (request.getType() == null) {
            throw new IllegalArgumentException("Request type is null");
        }
        final RequestType type;
        try {
            type = RequestType.valueOf(request.getType());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Unsupported type: " + request.getType());
        }

        if ((type == RequestType.TRANSFER || type == RequestType.BILL_PAYMENT)
                && request.getAmount() > Constants.DAILY_LIMIT
                && request.getAmount() < Constants.AML_THRESHOLD) {
            throw new IllegalArgumentException("Daily limit exceeded: " + request.getAmount());
        }

        if (request.getAmount() > Constants.AML_THRESHOLD) {
            throw new IllegalArgumentException("Blocked by AML");
        }

        if (type == RequestType.TRANSFER || type == RequestType.BILL_PAYMENT) {
            request.setAmount(request.getAmount() * (1 + Constants.COMMISSION_TRANSFER_RATE));
        }

        System.out.println("Processed: " + request);
    }
}
