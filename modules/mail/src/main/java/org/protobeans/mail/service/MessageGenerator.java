package org.protobeans.mail.service;

import java.util.Map;

import org.apache.commons.text.StringSubstitutor;

public class MessageGenerator {
    public String generateMessage(Map<String, Object> emailModel, String template) {
        StringSubstitutor substitutor = new StringSubstitutor(emailModel);

        return substitutor.replace(template);
    }
}
