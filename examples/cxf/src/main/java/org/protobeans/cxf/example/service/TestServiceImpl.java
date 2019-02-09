package org.protobeans.cxf.example.service;

import javax.jws.WebService;
import javax.validation.Valid;

import org.protobeans.cxf.example.model.TestRequest;
import org.protobeans.cxf.example.model.TestResponse;
import org.springframework.stereotype.Service;

@Service
@WebService(serviceName = "TestService", targetNamespace = "urn:test")
public class TestServiceImpl implements TestService {
    @Override
    public TestResponse test(@Valid TestRequest testRequest) throws Exception {
        return new TestResponse("Received: " + testRequest.getRequestData());
    }
}
