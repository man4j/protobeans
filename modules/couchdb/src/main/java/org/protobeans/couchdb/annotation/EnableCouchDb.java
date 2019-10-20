package org.protobeans.couchdb.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.protobeans.couchdb.config.CouchDbConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(CouchDbConfiguration.class)
@Configuration
public @interface EnableCouchDb {
    String dbHost();

    String dbPort() default "5984";

    String user() default "admin";

    String password();
}