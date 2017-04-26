package org.protobeans.monitoring.service.logsextractor;

import java.io.IOException;
import java.util.LinkedHashMap;

import org.protobeans.monitoring.model.logsextractor.ContainerLogMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.logstash.logback.marker.Markers;

public class LogOutputProcessingStage extends LogProcessingStage {
    private static final Logger logStashLogger = LoggerFactory.getLogger(LogOutputProcessingStage.class);
    
    private static final Logger simpleLogger = LoggerFactory.getLogger("simpleLogger");
    
    private ObjectMapper mapper = new ObjectMapper();
    
    @Override
    public void processLogMessage(ContainerLogMessage msg) {
        if (msg.getLine().contains("\"@timestamp\":")) {//this is LogStash JSON Message Line
            try {
                LinkedHashMap<String, Object> originalMessage = mapper.readValue(msg.getLine(), new TypeReference<LinkedHashMap<String, Object>>() {/*empty*/});
                
                originalMessage.putAll(msg.getMetadata());
                
                simpleLogger.info(mapper.writeValueAsString(originalMessage));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            logStashLogger.info(Markers.appendEntries(msg.getMetadata()), msg.getLine());
        }
    }
}
