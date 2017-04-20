package org.protobeans.monitoring.service.logsextractor;

import java.io.IOException;
import java.util.Map;

import org.protobeans.monitoring.model.logsextractor.ContainerLogMessage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonLineProcessingStage extends LogProcessingStage {
    private ObjectMapper mapper = new ObjectMapper();
    
    @Override
    public void processLogMessage(ContainerLogMessage msg) {
        if (msg.getLine().contains("\"@timestamp\":")) {//this is LogStash JSON Message Line
            try {
                msg.getMetadata().putAll(mapper.readValue(msg.getLine(), new TypeReference<Map<String, Object>>() {/*empty*/}));
                msg.setLine((String) msg.getMetadata().remove("message"));//remove original message for exclude duplicate
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        processNext(msg);
    }
}
