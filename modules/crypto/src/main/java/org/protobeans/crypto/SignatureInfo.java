package org.protobeans.crypto;

import java.util.regex.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SignatureInfo {
    private String serial;
    
    private String subject;
    
    private String inn;
    
    private String ogrn;
    
    private String snils;
    
    private long expiredDate;
    
    private long date;
    
    private String email;
    
    private String surname;
    
    private String nameMiddleName;
    
    private boolean legal;
    
    private String signedData;
    
    private boolean testCert;
    
    public String getFio() {
        return surname + " " + nameMiddleName;
    }

    public String getUserName() {
        var parts = parseGn();
        return parts.length > 0 ? parts[0] : "";
    }

    public String getUserMiddleName() {
        var parts = parseGn();
        return parts.length == 2 ? parts[1] : "";
    }

    private String[] parseGn() {
        if (nameMiddleName == null || nameMiddleName.isBlank()) {
            return new String[0];
        }

        var multispace = Pattern.compile("\\s+");
        var cleaned = multispace.matcher(nameMiddleName.replace('\u00A0', ' ').trim()).replaceAll(" ");
        return cleaned.split(" ", 2);
    }
}

