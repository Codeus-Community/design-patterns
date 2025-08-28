package org.codeus.design_patterns.cor.handler;

import org.codeus.design_patterns.cor.BankRequest;

public abstract class Handler {
    private Handler next;

    public void setNext(Handler next) {
        this.next = next;
    }

    public void handle(BankRequest request) {
        process(request);
        if (next != null) {
            next.handle(request);
        }
    }

    protected abstract void process(BankRequest request);
}
