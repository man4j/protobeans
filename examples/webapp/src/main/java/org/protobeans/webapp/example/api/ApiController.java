package org.protobeans.webapp.example.api;

import org.protobeans.mvc.rest.model.RestResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@OpenAPIDefinition(info = @Info(title = "API", version = "1.0"))
@RestController
@Validated
public interface ApiController {
    @GetMapping(value = "/api/version", produces = "text/html")
    @Tag(name = "Configuration methods")
    @Operation(summary = "Get version")
    @ApiResponse(responseCode = "200", description = "Successful")
    @ApiResponse(responseCode = "400", description = "Failed. Invalid input parameters", content = @Content(schema = @Schema(implementation = RestResult.class)))
    @ApiResponse(responseCode = "500", description = "Failed. Internal Server Error",    content = @Content(schema = @Schema(implementation = RestResult.class)))
    String getVersion() throws Exception;
}
