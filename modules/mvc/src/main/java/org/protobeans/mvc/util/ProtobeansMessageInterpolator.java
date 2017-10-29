package org.protobeans.mvc.util;

import java.util.Locale;

import org.hibernate.validator.internal.engine.messageinterpolation.ParameterTermResolver;
import org.hibernate.validator.messageinterpolation.AbstractMessageInterpolator;
import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;

public class ProtobeansMessageInterpolator extends AbstractMessageInterpolator {
    public ProtobeansMessageInterpolator() {
        super();
    }

    public ProtobeansMessageInterpolator(ResourceBundleLocator userResourceBundleLocator) {
        super( userResourceBundleLocator );
    }

    public ProtobeansMessageInterpolator(ResourceBundleLocator userResourceBundleLocator,
            ResourceBundleLocator contributorResourceBundleLocator) {
        super( userResourceBundleLocator, contributorResourceBundleLocator );
    }

    public ProtobeansMessageInterpolator(ResourceBundleLocator userResourceBundleLocator,
            ResourceBundleLocator contributorResourceBundleLocator,
            boolean cachingEnabled) {
        super( userResourceBundleLocator, contributorResourceBundleLocator, cachingEnabled );
    }

    public ProtobeansMessageInterpolator(ResourceBundleLocator userResourceBundleLocator, boolean cachingEnabled) {
        super( userResourceBundleLocator, null, cachingEnabled );
    }

    @Override
    public String interpolate(Context context, Locale locale, String term) {
        ParameterTermResolver parameterTermResolver = new ParameterTermResolver();
        return parameterTermResolver.interpolate( context, term );
    }
}

