package org.protobeans.cxf.example.service;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.validation.Valid;

import org.apache.cxf.annotations.SchemaValidation;
import org.apache.cxf.annotations.WSDLDocumentation;
import org.protobeans.cxf.example.model.TestRequest;
import org.protobeans.cxf.example.model.TestResponse;

@WebService(serviceName = "TestService", targetNamespace = "urn:test")
@SOAPBinding(parameterStyle = ParameterStyle.BARE)
@SchemaValidation
@WSDLDocumentation("Тестовый сервис")
public interface TestService {
    @WSDLDocumentation("Тестовый метод")
    @WebResult(name="testResponse")
    TestResponse test(@Valid @WebParam(name = "testRequest") TestRequest testRequest) throws Exception;
}
