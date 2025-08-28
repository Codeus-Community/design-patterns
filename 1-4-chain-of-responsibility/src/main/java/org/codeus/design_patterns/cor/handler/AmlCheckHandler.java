package org.codeus.design_patterns.cor.handler;

import org.codeus.design_patterns.cor.BankRequest;
import org.codeus.design_patterns.cor.Constants;

public class AmlCheckHandler extends Handler {
    @Override
    protected void process(BankRequest request) {
        if (request.getAmount() > Constants.AML_THRESHOLD) {
            throw new IllegalArgumentException("Blocked by AML");
        }
    }
}
