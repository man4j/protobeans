package org.protobeans.undertow.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import io.undertow.predicate.Predicate;
import io.undertow.server.HttpServerExchange;

public class CompressibleMimeTypePredicate implements Predicate {
    private final List<MimeType> mimeTypes = new ArrayList<>();

    public CompressibleMimeTypePredicate() {
        var mimeStringTypes = List.of(
        "text/html",
        "text/xml",
        "text/plain",
        "text/css",
        "text/javascript",
        "application/javascript",
        "application/json");
        
        for (String mimeTypeString : mimeStringTypes) {
            this.mimeTypes.add(MimeTypeUtils.parseMimeType(mimeTypeString));
        }
    }

    @Override
    public boolean resolve(HttpServerExchange value) {
        String contentType = value.getResponseHeaders().getFirst("Content-Type");

        if (contentType != null) {
            for (MimeType mimeType : this.mimeTypes) {
                if (mimeType.isCompatibleWith(MimeTypeUtils.parseMimeType(contentType))) {
                    return true;
                }
            }
        }

        return false;
    }
}
