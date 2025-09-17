//package org.codeus.design_patterns.state;
//
//
//import org.codeus.design_patterns.state.clean.DocumentWorkflow;
//
//import java.util.UUID;
//
///** Runs the contract against the clean State-pattern implementation. */
//public class CleanWorkflowContractTest extends AbstractWorkflowContractTest {
//    @Override
//    protected WorkflowContract newWorkflow(DocCase dc) {
//        return new DocumentWorkflow(
//                UUID.randomUUID().toString(),
//                dc.type(),
//                dc.content(),
//                dc.author(),
//                dc.urgent()
//        );
//    }
//}
