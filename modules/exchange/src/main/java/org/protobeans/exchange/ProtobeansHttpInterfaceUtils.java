package org.protobeans.exchange;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;

import tools.jackson.core.json.JsonReadFeature;
import tools.jackson.databind.json.JsonMapper;


public class ProtobeansHttpInterfaceUtils {
    public static JsonMapper mapper() {
        return JsonMapper.builder().configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS, true)
                                   .changeDefaultVisibility(vc -> vc.withVisibility(PropertyAccessor.FIELD,    Visibility.ANY)
                                                                             .withVisibility(PropertyAccessor.GETTER,   Visibility.NONE)
                                                                             .withVisibility(PropertyAccessor.IS_GETTER,Visibility.NONE)
                                                                             .withVisibility(PropertyAccessor.SETTER,   Visibility.NONE)
                                                                             .withVisibility(PropertyAccessor.CREATOR,  Visibility.NONE))
                                   .changeDefaultPropertyInclusion(v -> v.withValueInclusion(JsonInclude.Include.NON_NULL))
                                   .build();
    }
}
