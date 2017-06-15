package org.protobeans.testcontainers.mysql.listener;

import org.protobeans.testcontainers.mysql.annotation.EnablePerconaContainer;
import org.protobeans.testcontainers.mysql.container.PerconaContainer;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class PerconaContainerListener extends AbstractTestExecutionListener {
    private PerconaContainer<?> percona;
    
    @Override
    @SuppressWarnings("rawtypes")
    public void beforeTestClass(TestContext testContext) throws Exception {
        EnablePerconaContainer annotation = testContext.getTestClass().getAnnotation(EnablePerconaContainer.class);
        
        percona = new PerconaContainer(annotation.schema(), annotation.rootPassword(), annotation.skipInit(), annotation.imageTag());
        percona.start();
        
        System.setProperty(annotation.exposePasswordAs(), annotation.rootPassword());
        System.setProperty(annotation.exposeUrlAs(), percona.getJdbcUrl());
        System.setProperty(annotation.exposeSchemaAs(), annotation.schema());
    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        if (percona != null) {
            percona.stop();
        }
    }
}
