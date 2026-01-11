package org.protobeans.undertow.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import io.undertow.predicate.Predicate;
import io.undertow.server.HttpServerExchange;

public class CompressibleMimeTypePredicate implements Predicate {
    private final List<MimeType> mimeTypes;

    public CompressibleMimeTypePredicate(String... mimeTypes) {
        this.mimeTypes = new ArrayList<>(mimeTypes.length);
        for (String mimeTypeString : mimeTypes) {
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
