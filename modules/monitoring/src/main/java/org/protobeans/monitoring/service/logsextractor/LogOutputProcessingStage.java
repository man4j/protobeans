package org.protobeans.monitoring.service.logsextractor;

import org.protobeans.monitoring.model.logsextractor.ContainerLogMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.logstash.logback.marker.Markers;

public class LogOutputProcessingStage extends LogProcessingStage {
    private static final Logger logger = LoggerFactory.getLogger(LogOutputProcessingStage.class);

    @Override
    public void processLogMessage(ContainerLogMessage msg) {
        logger.info(Markers.appendEntries(msg.getMetadata()), msg.getLine());
    }
}
