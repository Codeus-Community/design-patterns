package org.codeus.design_patterns.cor.handler;

import org.codeus.design_patterns.cor.BankRequest;
import org.codeus.design_patterns.cor.RequestType;

public class ValidationHandler extends Handler {
    @Override
    protected void process(BankRequest request) {
        String type = request.getType();
        if (type == null) {
            throw new IllegalArgumentException("Request type is null");
        }
        try {
            RequestType.valueOf(type);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }
}
