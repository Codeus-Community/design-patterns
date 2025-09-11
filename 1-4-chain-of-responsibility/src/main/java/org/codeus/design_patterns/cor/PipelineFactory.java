package org.codeus.design_patterns.cor;

import org.codeus.design_patterns.cor.handler.*;

public class PipelineFactory {

    public static Handler createPipeline(RequestType type) {
        Handler validation = new ValidationHandler();
        Handler limit = new LimitCheckHandler();
        Handler aml = new AmlCheckHandler();
        Handler commission = new CommissionHandler();
        Handler logging = new LoggingHandler();

        if (type == RequestType.CREDIT_APPLICATION) {
            validation.setNext(aml);
            aml.setNext(logging);
            return validation;
        }

        validation.setNext(limit);
        limit.setNext(aml);
        aml.setNext(commission);
        commission.setNext(logging);
        return validation;
    }
}
