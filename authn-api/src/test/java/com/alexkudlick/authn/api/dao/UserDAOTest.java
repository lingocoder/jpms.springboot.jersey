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
import org.junit.Before;
import org.junit.Rule;
/*import org.springframework.security.crypto.password.PasswordEncoder;*/

import com.atlassian.security.password.PasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.query.NativeQuery;

@RunWith( SpringRunner.class )
@SpringBootTest( classes = { HibernateConfigurator.class } )
@Transactional
public class UserDAOTest {

    private PasswordEncoder passwordEncoder;
    private UserDAO dao;

    @Autowired
    private SessionFactory sessionFactory;

    @Before
    public void setUp( ) {
        passwordEncoder = mock( PasswordEncoder.class );
        dao = new UserDAO( sessionFactory, passwordEncoder );
    }

    @Test
    public void contextLoads( ) {
    }

    @Test
    public void testCreateUser( ) {
        when( passwordEncoder.encodePassword( anyString( ) ) ).thenReturn( "ENCRYPTED" );

        Session session = sessionFactory.getCurrentSession( );

        createUser( "user123", "password123" );
        String sql = "SELECT * FROM USERS WHERE username = :user_name";
        NativeQuery query = session.createNativeQuery( sql );
        query.addEntity( UserEntity.class );
        query.setParameter( "user_name", "user123" );
        UserEntity entity = ( UserEntity ) query.uniqueResult( );
        /*        UserEntity entity = session.get(UserEntity.class, 0L);*/
        assertEquals( "user123", entity.getUserName( ) );
        assertEquals( "ENCRYPTED", entity.getEncryptedPassword( ) );

        verify( passwordEncoder ).encodePassword( "password123" );
        verifyNoMoreInteractions( passwordEncoder );
    }

    @Test
    public void testCheckLoginWithValidUserNameAndPassword( ) {
        saveUser( "userFoo", "encryptedPassword1" );
        when( passwordEncoder.isValidPassword( anyString( ), anyString( ) ) ).thenReturn( true );

        assertTrue( dao.isValidLogin( "userFoo", "rawPassword" ) );

        verify( passwordEncoder ).isValidPassword( "rawPassword", "encryptedPassword1" );
        verifyNoMoreInteractions( passwordEncoder );
    }

    @Test
    public void testCheckLoginWithIncorrectPassword( ) {
        saveUser( "userASDF", "encryptedPassword2" );
        when( passwordEncoder.isValidPassword( anyString( ), anyString( ) ) ).thenReturn( false );

        assertFalse( dao.isValidLogin( "userASDF", "rawPassword" ) );

        verify( passwordEncoder ).isValidPassword( "rawPassword", "encryptedPassword2" );
        verifyNoMoreInteractions( passwordEncoder );
    }

    @Test
    public void testCheckLoginWithNonExistentUserName( ) {
        assertFalse( dao.isValidLogin( "userFoo", "rawPassword" ) );
        verifyNoMoreInteractions( passwordEncoder );
    }

    private void saveUser( String userName, String encryptedPassword ) {

        Session session = sessionFactory.getCurrentSession( );

        UserEntity.withUnsavedInstance(
                userName,
                encryptedPassword,
                session::persist
        );
    }

    private void createUser( String name, String pwd ) {
        dao.createUser( name, pwd );
    }
}