package org.codeus.design_patterns.cor.handler;

import org.codeus.design_patterns.cor.BankRequest;

public class LoggingHandler extends Handler {
    @Override
    protected void process(BankRequest request) {
        System.out.println("Processed: " + request);
    }
}