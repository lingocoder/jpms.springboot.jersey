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
import com.google.common.cache.Cache;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class AuthenticationTokenManager {

    private final Cache< String, String > tokenCache;
    private final UserDAO userDAO;
    private final Supplier< UUID > tokenGenerator;

    public AuthenticationTokenManager( Cache< String, String > tokenCache, UserDAO userDAO, Supplier< UUID > tokenGenerator ) {
        this.tokenCache = Objects.requireNonNull( tokenCache );
        this.userDAO = Objects.requireNonNull( userDAO );
        this.tokenGenerator = Objects.requireNonNull( tokenGenerator );
    }

    public boolean isValid( String token ) {
        return tokenCache.getIfPresent( token ) != null;
    }

    public Optional< AuthenticationToken > login( String userName, String password ) {
        if ( userDAO.isValidLogin( userName, password ) ) {
            String token = tokenGenerator.get( ).toString( );
            tokenCache.put( token, token );
            return Optional.of( new AuthenticationToken( token ) );
        } else {
            return Optional.empty( );
        }
    }
}
