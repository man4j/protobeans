package org.protobeans.cxf.example.model;

import javax.validation.constraints.NotBlank;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CatalogProductRequest")
public class TestRequest {
    @XmlElement(required = true)
    @NotBlank(message = "Field not be blank")
    private String requestData;
    
    public TestRequest(String requestData) {
        this.requestData = requestData;
    }
    
    public TestRequest() {
        //empty
    }
    
    public String getRequestData() {
        return requestData;
    }
}
