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

import static org.springframework.http.HttpStatus.CREATED;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;
import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.http.MediaType.TEXT_XML;
import static org.springframework.http.MediaType.APPLICATION_XML;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

import com.alexkudlick.authentication.models.AuthenticationRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;
import java.util.ArrayList;

import static java.util.Arrays.asList;

@SpringBootTest( webEnvironment = WebEnvironment.RANDOM_PORT )
@RunWith( SpringRunner.class )
public class UserResourceIntegrationTest {

    @LocalServerPort
    private int port;

    @Test
    public void testCreateUser( ) throws URISyntaxException {

        AuthenticationRequest body = new AuthenticationRequest( "master.jar", "testPassword" );

        URI uri = new URI( "http://localhost:" + port + "/api/users" );

        RequestEntity request = RequestEntity
                .post( uri )
                .accept( APPLICATION_JSON )
                .body( body );

        RestTemplate client = configureMsgConverters( );

        ResponseEntity< ? > response = client.exchange( request, Object.class );

        assertThat( response.getStatusCode( ) ).isEqualTo( CREATED );
    }

    private RestTemplate configureMsgConverters( ) {

        RestTemplate inner = new RestTemplate( );

        List< HttpMessageConverter< ? > > messageConverters = new ArrayList<>( );
        //Add the Jackson Message converter
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter( );

        // Note: here we are making this converter to process any kind of response,
        // not only application/*json, which is the default behaviour

        converter.setSupportedMediaTypes( asList( APPLICATION_JSON, TEXT_HTML, TEXT_PLAIN, TEXT_XML, APPLICATION_XML, APPLICATION_JSON_UTF8, APPLICATION_OCTET_STREAM ) );
        messageConverters.add( converter );

        inner.setMessageConverters( messageConverters );

        return inner;
    }
}