package org.codeus.design_patterns.state;

import org.codeus.design_patterns.state.dirty.HellWorkflowAdapter;

import java.util.UUID;
/**
 * Runs the same behavioral contract tests against the legacy implementation
 * via the adapter.
 */
public class HellWorkflowContractTest extends AbstractWorkflowContractTest {
    @Override
    protected WorkflowContract newWorkflow(DocCase dc) {
        return new HellWorkflowAdapter(
                UUID.randomUUID().toString(),
                dc.type(),
                dc.content(),
                dc.author(),
                dc.urgent()
        );
    }
}
