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
package com.alexkudlick.authn.api.config;

import com.alexkudlick.authn.api.tokens.AuthenticationTokenManager;
import com.google.common.cache.CacheBuilder;
import com.alexkudlick.authn.api.dao.UserDAO;
import com.google.common.cache.Cache;
import org.hibernate.SessionFactory;

import java.util.UUID;

import com.atlassian.security.password.PasswordEncoder;
import com.atlassian.security.password.DefaultPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthenticationConfiguration {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private UserDAO userDAO;

    @Bean
    public AuthenticationTokenManager tokenManager( ) {

        String cacheSpec = "maximumSize=10000, expireAfterAccess=1h";

        Cache< String, String > tokenCache = CacheBuilder.from( cacheSpec ).build( );

        AuthenticationTokenManager tokenManager = new AuthenticationTokenManager( tokenCache, userDAO, UUID::randomUUID );

        return tokenManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder( ) {
        return DefaultPasswordEncoder.getDefaultInstance( );
    }
}
