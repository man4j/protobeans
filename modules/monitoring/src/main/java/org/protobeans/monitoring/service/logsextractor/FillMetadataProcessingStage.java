package org.protobeans.monitoring.service.logsextractor;

import java.util.Map.Entry;

import org.protobeans.monitoring.model.ContainerInfo;
import org.protobeans.monitoring.model.logsextractor.ContainerLogMessage;

public class FillMetadataProcessingStage extends LogProcessingStage {
    @Override
    public void processLogMessage(ContainerLogMessage msg) {
        ContainerInfo info = msg.getContainerInfo();
        
        msg.getMetadata().put("logs_service_name", info.getService().spec().name());
        msg.getMetadata().put("logs_node_id", info.getTask().nodeId());
        msg.getMetadata().put("logs_node_name", info.getNode().description().hostname());
        msg.getMetadata().put("logs_container_id", info.getContainer().id());
        msg.getMetadata().put("logs_container_name", info.getContainerName());
        msg.getMetadata().put("logs_msg_type", msg.getLogChannel().name());
        msg.getMetadata().put("logs_service_pattern", msg.getContainerInfo().getMonitoringPattern());
        
        if (info.getNode().spec().labels() != null) {
            for (Entry<String, String> e : info.getNode().spec().labels().entrySet()) {
                msg.getMetadata().put("logs_node_labels_" + e.getKey(), e.getValue());
            }
        }
        
        processNext(msg);
    }
}
