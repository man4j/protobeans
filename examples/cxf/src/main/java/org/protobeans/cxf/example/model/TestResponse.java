package org.protobeans.cxf.example.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProductCatalogResponse")
public class TestResponse {
    @XmlElement(required = true, name = "result")
    private String result;
    
    public TestResponse(String result) {
        this.result = result;
    }

    public TestResponse() {
        // empty
    }

    public String getResult() {
        return result;
    }
}
