package org.codeus.design_patterns.cor;

import org.codeus.design_patterns.cor.handler.Handler;

public class BankRequestProcessor {
    public void process(BankRequest request) {
        Handler pipeline = PipelineFactory.createPipeline(request.getType());
        pipeline.handle(request);
    }
}
