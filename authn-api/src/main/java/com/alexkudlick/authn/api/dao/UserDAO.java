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

import com.alexkudlick.authn.api.entities.UserEntity;
import org.hibernate.SessionFactory;
import com.atlassian.security.password.PasswordEncoder;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Objects;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class UserDAO extends AbstractHibernateDao< UserEntity > {

    private final PasswordEncoder passwordEncoder;

    public UserDAO( SessionFactory sessionFactory, PasswordEncoder passwordEncoder ) {
        super( sessionFactory );
        this.passwordEncoder = Objects.requireNonNull( passwordEncoder );
    }

    public void createUser( String userName, String password ) {

        Objects.requireNonNull( userName );

        Objects.requireNonNull( password );

        UserEntity.withUnsavedInstance(
                userName,
                passwordEncoder.encodePassword( password ),
                this::persist
        );
    }

    public boolean isValidLogin( String userName, String password ) {

        CriteriaBuilder criteriaBuilder = getCurrentSession( ).getCriteriaBuilder( );

        CriteriaQuery< UserEntity > query = criteriaBuilder.createQuery( UserEntity.class );

        Root< UserEntity > root = query.from( UserEntity.class );

        try {
            UserEntity entity = getCurrentSession( )
                    .createQuery( query.where( criteriaBuilder.equal( root.get( "userName" ), userName ) ) )
                    .getSingleResult( );

            return passwordEncoder.isValidPassword( password, entity.getEncryptedPassword( ) );

        } catch ( NoResultException e ) {
            return false;
        }
    }
}
