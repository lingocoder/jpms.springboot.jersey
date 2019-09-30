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
package com.alexkudlick.authn.api.dao;

import org.hsqldb.jdbc.JDBCDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.sql.DataSource;
import java.util.Properties;

import static java.util.Objects.requireNonNull;

@Configuration
@EnableTransactionManagement
@PropertySource( { "classpath:application.properties" } )
@EnableAutoConfiguration
public class HibernateConfigurator {

    @Autowired
    private Environment env;

    @Bean
    public LocalSessionFactoryBean sessionFactory( ) {

        final LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean( );

        sessionFactory.setDataSource( dataSource( ) );

        sessionFactory.setPackagesToScan( "com.alexkudlick.authn.api", "com.alexkudlick.authentication.models" );

        return sessionFactory;
    }

    @Bean
    public DataSource dataSource( ) {

        JDBCDataSource dataSource = new JDBCDataSource( );

        dataSource.setUrl( requireNonNull( env.getProperty( "spring.datasource.url" ) ) );

        dataSource.setUser( requireNonNull( env.getProperty( "spring.datasource.username" ) ) );

        dataSource.setPassword( requireNonNull( env.getProperty( "spring.datasource.password" ) ) );

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager hibernateTransactionManager( ) {

        final HibernateTransactionManager transactionManager = new HibernateTransactionManager( );

        transactionManager.setSessionFactory( sessionFactory( ).getObject( ) );

        return transactionManager;
    }

    private final Properties hibernateProperties( ) {

        final Properties hibernateProperties = new Properties( );

        hibernateProperties.setProperty( "hibernate.hbm2ddl.auto", env.getProperty( "hibernate.hbm2ddl.auto" ) );

        hibernateProperties.setProperty( "hibernate.dialect", env.getProperty( "hibernate.dialect" ) );

        return hibernateProperties;
    }
}