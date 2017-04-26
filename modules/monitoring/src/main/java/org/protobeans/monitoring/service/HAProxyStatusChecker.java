package org.protobeans.monitoring.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.protobeans.monitoring.model.MonitoringProtocol;
import org.protobeans.monitoring.model.MonitoringType;
import org.protobeans.monitoring.model.haproxy.HAProxyStat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.springframework.stereotype.Service;

import com.spotify.docker.client.messages.swarm.Node;
import com.spotify.docker.client.messages.swarm.Task;

import net.logstash.logback.marker.Markers;

/**
 * For more information about HAProxy see https://logz.io/blog/monitor-haproxy-elk-stack/ 
 */
@Service
public class HAProxyStatusChecker implements StatusChecker {
    private static final Logger logger = LoggerFactory.getLogger(HAProxyStatusChecker.class);
    
    @Override
    public void checkStatus(String ip, int port, Task task, Node node) throws Exception {
        String result = sendCommand(ip, port, "show info");
        
        Map<String, String> infoMap = Arrays.stream(result.split("\n")).collect(Collectors.toMap(l -> l.split(":")[0].trim(), l -> l.split(":")[1].trim()));

        String nodeId = infoMap.get("node");
        
        result = sendCommand(ip, port, "show stat");
        
        List<HAProxyStat> components = new ArrayList<>();
        
        for (String csvLine : result.split("\n")) {
            String[] arr = csvLine.split(",");
            
            if (!arr[0].startsWith("#") && !arr[0].startsWith("stats") && !arr[1].startsWith("FRONTEND") && !arr[1].startsWith("BACKEND")) {
                HAProxyStat stat = new HAProxyStat();
                
                stat.setNode(nodeId);
                stat.setPxname(arr[0]);
                stat.setSvname(arr[1]);
                stat.setQcur(Integer.parseInt(arr[2]));
                stat.setScur(Integer.parseInt(arr[4]));
                stat.setStatus(arr[17]);
                
                components.add(stat);
            }
        }

        logStats(components, node);
    }
    
    private void logStats(List<HAProxyStat> components, Node node) {
        for (HAProxyStat stat : components) {
            Marker marker = Markers.append("monitoringType", MonitoringType.HAPROXY.name()).and(
                            Markers.append("haproxy_node", stat.getNode())).and(
                            Markers.append("haproxy_pxname", stat.getPxname())).and(
                            Markers.append("haproxy_svname", stat.getSvname())).and(
                            Markers.append("haproxy_qcur", stat.getQcur())).and(
                            Markers.append("haproxy_scur", stat.getScur())).and(
                            Markers.append("haproxy_status", stat.getStatus()));
            
            if (node.spec().labels() != null) {
                for (Entry<String, String> e : node.spec().labels().entrySet()) {
                    marker.add(Markers.append("haproxy_node_labels_" + e.getKey(), e.getValue()));
                }
            }
            
            logger.info(marker, "");
        }
    }
    
    private String sendCommand(String ip, int port, String command) throws Exception {
        StringBuilder sb = new StringBuilder();
        
        try (Socket socket = new Socket(ip, port);
             OutputStream out = socket.getOutputStream();
             InputStream in = socket.getInputStream()) {
            out.write((command + "\n").getBytes());
            
            int c;
            
            while ((c = socket.getInputStream().read()) != -1) {
                sb.append((char) c);
            }
        }
        
        return sb.toString();
    }

    @Override
    public MonitoringType getMonitoringType() {
        return MonitoringType.HAPROXY;
    }

    @Override
    public MonitoringProtocol getMonitoringProtocol() {
        return MonitoringProtocol.HAPROXY;
    }
}
