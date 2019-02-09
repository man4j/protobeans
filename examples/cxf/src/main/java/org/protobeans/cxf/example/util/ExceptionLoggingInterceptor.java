package org.protobeans.cxf.example.util;

import javax.xml.namespace.QName;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionLoggingInterceptor extends AbstractSoapInterceptor {
    private static Logger logger = LoggerFactory.getLogger(ExceptionLoggingInterceptor.class);

    public ExceptionLoggingInterceptor() {
        super(Phase.MARSHAL);
    }

    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        Fault f = (Fault) message.getContent(Exception.class);

        Throwable cause = f.getCause();
        
        if (cause instanceof Exception) {
            f.setFaultCode(QName.valueOf("500"));
            f.setMessage(cause.getMessage());
            f.setStatusCode(500);
//            f.setDetail(details);
            
            logger.error("", cause);
        }
    }
}
