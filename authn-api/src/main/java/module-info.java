/**
 * A Gradle plugin that supports compiling, testing, assembling and maintaining Modular Multi-Release JAR Files.
 *
 * Copyright (C) 2019 lingocoder <plugins@lingocoder.com>
 *
 * This work is licensed under the Creative Commons Attribution-NoDerivatives 4.0
 * International (CC BY-ND 4.0) License.
 *
 * This work is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Creative Commons Attribution-NoDerivatives 4.0 International (CC BY-ND 4.0)
 * License for more details.
 *
 * You should have received a copy of the Creative Commons
 * Attribution-NoDerivatives 4.0 International (CC BY-ND 4.0) License
 * along with this program. To view a copy of this license,
 * visit https://creativecommons.org/licenses/by-nd/4.0/.
 */
module mr.jar.authn.api {
    requires com.alexkudlick.authentication.models;

    requires jackson.annotations;
    requires com.google.common;
    requires java.ws.rs;
    requires java.naming;

    requires java.sql;
    requires org.hibernate.orm.core;
    requires com.fasterxml.jackson.databind;
    requires org.hibernate.validator;

    opens com.alexkudlick.authn.api.config to com.fasterxml.jackson.databind;
    opens com.alexkudlick.authn.api.web to jersey.server;
    opens com.alexkudlick.authn.api.entities to org.hibernate.orm.core, javassist;

    /* mr.jar */
    requires hsqldb;
    requires spring.data.jpa;
    requires spring.data.commons;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.core;
    requires spring.context;
    requires spring.beans;
    requires spring.orm;
    requires spring.web;
    requires spring.tx;
    requires spring.security.config;
    requires org.hibernate.commons.annotations;
    requires jersey.media.jaxb;
    requires jersey.media.json.jackson;
    requires atlassian.password.encoder;
    requires java.persistence;
}