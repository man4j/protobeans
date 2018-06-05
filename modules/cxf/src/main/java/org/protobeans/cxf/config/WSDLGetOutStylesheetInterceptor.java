package org.protobeans.cxf.config;

import org.apache.cxf.frontend.WSDLGetInterceptor;
import org.apache.cxf.frontend.WSDLGetOutInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.w3c.dom.Document;
import org.w3c.dom.ProcessingInstruction;

public class WSDLGetOutStylesheetInterceptor extends WSDLGetOutInterceptor {
    @Override
    public void handleMessage(Message message) throws Fault {
        Document doc = (Document) message.get(WSDLGetInterceptor.DOCUMENT_HOLDER);
        
        if (doc == null) {
            return;
        }
        
        ProcessingInstruction pi = doc.createProcessingInstruction("xml-stylesheet", "type='text/xsl' href='wsdl-viewer.xsl'");
        
        doc.insertBefore(pi, doc.getFirstChild());
        
        super.handleMessage(message);
    }
}
