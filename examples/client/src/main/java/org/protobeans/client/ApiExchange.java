package org.protobeans.client;

import org.protobeans.client.model.Document;
import org.protobeans.exchange.model.RestResult;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;

// @RestController <----------- обратите внимание эта аннотация больше не нужна
@Validated
@OpenAPIDefinition(info = @Info(title = "API", version = "1.0"), security = @SecurityRequirement(name = "basicAuth"))
@SecurityScheme(name = "basicAuth", type = SecuritySchemeType.HTTP, scheme = "Basic")
@Tag(name = "Documents API")
@ApiResponse(responseCode = "200", description = "Successful")
@ApiResponse(responseCode = "401", description = "Unauthrorized", content = @Content(schema = @Schema(implementation = RestResult.class)))
@ApiResponse(responseCode = "400", description = "Failed. Invalid input parameters", content = @Content(schema = @Schema(implementation = RestResult.class)))
@ApiResponse(responseCode = "500", description = "Failed. Internal Server Error",    content = @Content(schema = @Schema(implementation = RestResult.class)))
@HttpExchange(value = "/api/documents") // <----------- обратите внимание на эту аннотацию
public interface ApiExchange {
    @Secured("ROLE_ADMIN")
    @Operation(summary = "Save document")
    @PostExchange(value = "/saveDocument", contentType = "application/json") // <----------- обратите внимание на эту аннотацию
    void saveDocument(@Validated 
                      @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "A document") 
                      @RequestBody Document document);
    
    @Secured("ROLE_ADMIN")
    @Operation(summary = "Get document")    
    @GetExchange(value = "/getDocument", accept = "application/json") // <----------- обратите внимание на эту аннотацию
    Document getDocument(@RequestParam @NotBlank String docId);
        
    @Secured("ROLE_SUPER_ADMIN")
    @Operation(summary = "Super secured method")
    @GetExchange(value = "/topSecret")
    void superSecuredMethod();
}
