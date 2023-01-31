package org.protobeans.webapp.example.feign;

import org.protobeans.feign.config.BaseFeignFactory;
import org.protobeans.webapp.example.api.ApiController;
import org.springframework.stereotype.Component;

@Component
public class AppApiFactory extends BaseFeignFactory<ApiController> {
    //empty
}
