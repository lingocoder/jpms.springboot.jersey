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
package com.alexkudlick.authn.api.web;

import com.alexkudlick.authn.api.dao.UserDAO;
import com.alexkudlick.authentication.models.AuthenticationRequest;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class UserResourceTest {

    private UserDAO dao;
    private UserResource resource;

    @Before
    public void setUp( ) {
        dao = mock( UserDAO.class );
        resource = new UserResource( dao );
    }

    @Test
    public void testExecute( ) throws Exception {
        resource.createUser( new AuthenticationRequest( "foo", "bar" ) );

        verify( dao ).createUser( "foo", "bar" );
        verifyNoMoreInteractions( dao );
    }
}