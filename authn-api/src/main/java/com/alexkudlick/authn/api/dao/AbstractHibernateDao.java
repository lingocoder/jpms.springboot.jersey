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

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.SessionFactory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings( "unchecked" )
@Transactional
public abstract class AbstractHibernateDao< T extends Serializable > {

    private Class< T > clazz;

    private SessionFactory sessionFactory;

    @Autowired
    public AbstractHibernateDao( SessionFactory sessionFactory ) {
        this.sessionFactory = sessionFactory;
    }

    public void setClazz( Class< T > clazzToSet ) {
        this.clazz = clazzToSet;
    }

    public T findOne( long id ) {
        return getCurrentSession( ).get( clazz, id );
    }

    public List findAll( ) {
        return getCurrentSession( ).createQuery( "from " + clazz.getName( ) ).list( );
    }

    public T persist( T entity ) {
        getCurrentSession( ).saveOrUpdate( entity );
        return entity;
    }

    public T update( T entity ) {
        return ( T ) getCurrentSession( ).merge( entity );
    }

    public void delete( T entity ) {
        getCurrentSession( ).delete( entity );
    }

    public void deleteById( long entityId ) {
        T entity = findOne( entityId );
        delete( entity );
    }

    protected Session getCurrentSession( ) {
        return sessionFactory.getCurrentSession( );
    }
}