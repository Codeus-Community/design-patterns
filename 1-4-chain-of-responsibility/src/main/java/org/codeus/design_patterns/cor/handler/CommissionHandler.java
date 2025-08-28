package org.codeus.design_patterns.cor.handler;

import org.codeus.design_patterns.cor.BankRequest;
import org.codeus.design_patterns.cor.Constants;

public class CommissionHandler extends Handler {
    @Override
    protected void process(BankRequest request) {
        request.setAmount(request.getAmount() * (1 + Constants.COMMISSION_TRANSFER_RATE));
    }
}
