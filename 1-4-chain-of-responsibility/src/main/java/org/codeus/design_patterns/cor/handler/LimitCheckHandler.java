package org.codeus.design_patterns.cor.handler;

import org.codeus.design_patterns.cor.BankRequest;
import org.codeus.design_patterns.cor.Constants;

public class LimitCheckHandler extends Handler {
    @Override
    protected void process(BankRequest request) {
        if (request.getAmount() > Constants.DAILY_LIMIT
                && request.getAmount() < Constants.AML_THRESHOLD) {
            throw new IllegalArgumentException("Daily limit exceeded: " + request.getAmount());
        }
    }
}
