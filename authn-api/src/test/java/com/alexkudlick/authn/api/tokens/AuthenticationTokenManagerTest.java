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
package com.alexkudlick.authn.api.tokens;

import com.alexkudlick.authn.api.dao.UserDAO;
import com.alexkudlick.authentication.models.AuthenticationToken;
import com.google.common.cache.LoadingCache;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@SuppressWarnings( "unchecked" )
public class AuthenticationTokenManagerTest {

    private LoadingCache< String, String > cache;
    private AuthenticationTokenManager manager;
    private UserDAO userDAO;
    private Supplier< UUID > tokenGenerator;

    @Before
    public void setUp( ) {
        cache = mock( LoadingCache.class );
        userDAO = mock( UserDAO.class );
        tokenGenerator = mock( Supplier.class );
        manager = new AuthenticationTokenManager( cache, userDAO, tokenGenerator );
    }

    @Test
    public void testTokenIsValidIfInCache( ) {
        when( cache.getIfPresent( anyString( ) ) ).thenReturn( "asdfasdfasdf" );

        assertTrue( manager.isValid( "asdfasdfasdf" ) );

        verify( cache ).getIfPresent( "asdfasdfasdf" );
        verifyNoMoreInteractions( cache, userDAO, tokenGenerator );
    }

    @Test
    public void testTokenIsInValidIfNotInCache( ) {
        when( cache.getIfPresent( anyString( ) ) ).thenReturn( null );

        assertFalse( manager.isValid( "asdfasdfasdf" ) );

        verify( cache ).getIfPresent( "asdfasdfasdf" );
        verifyNoMoreInteractions( cache, userDAO, tokenGenerator );
    }

    @Test
    public void testLoginWithValidUserNameAndPassword( ) {
        UUID token = UUID.randomUUID( );

        when( userDAO.isValidLogin( anyString( ), anyString( ) ) ).thenReturn( true );
        when( tokenGenerator.get( ) ).thenReturn( token );

        assertEquals(
                Optional.of( new AuthenticationToken( token.toString( ) ) ),
                manager.login( "hello", "world" )
        );

        verify( userDAO ).isValidLogin( "hello", "world" );
        verify( tokenGenerator ).get( );
        verify( cache ).put( token.toString( ), token.toString( ) );
        verifyNoMoreInteractions( cache, userDAO, tokenGenerator );
    }

    @Test
    public void testLoginWithInvalidUserNameAndPassword( ) {
        when( userDAO.isValidLogin( anyString( ), anyString( ) ) ).thenReturn( false );

        assertEquals(
                Optional.empty( ),
                manager.login( "hello", "world" )
        );

        verify( userDAO ).isValidLogin( "hello", "world" );
        verifyNoMoreInteractions( cache, userDAO, tokenGenerator );
    }
}