package org.protobeans.monitoring.model.haproxy;

public class HAProxyStat {
    private String node;
    
    private String pxname;
    
    private String svname;
    
    private int qcur;
    
    private int scur;
    
    private String status;
    
    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getPxname() {
        return pxname;
    }

    public void setPxname(String pxname) {
        this.pxname = pxname;
    }

    public String getSvname() {
        return svname;
    }

    public void setSvname(String svname) {
        this.svname = svname;
    }

    public int getQcur() {
        return qcur;
    }

    public void setQcur(int qcur) {
        this.qcur = qcur;
    }

    public int getScur() {
        return scur;
    }

    public void setScur(int scur) {
        this.scur = scur;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
