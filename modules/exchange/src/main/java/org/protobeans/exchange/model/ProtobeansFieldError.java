package org.protobeans.exchange.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ProtobeansFieldError {
    private String fieldName;
    
    private String fieldError;
}
