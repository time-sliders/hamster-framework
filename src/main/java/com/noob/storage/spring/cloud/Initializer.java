package com.noob.storage.spring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ldap.LdapAutoConfiguration;
import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

/**
 * @author luyun
 * @since 2018.11.28 11:11
 */
@SpringBootConfiguration
@EnableAutoConfiguration(exclude = {
        LdapAutoConfiguration.class,
        EmbeddedLdapAutoConfiguration.class,
        MongoAutoConfiguration.class})
public class Initializer {

    public static void main(String[] args) {
        SpringApplication.run(Initializer.class);
    }
}