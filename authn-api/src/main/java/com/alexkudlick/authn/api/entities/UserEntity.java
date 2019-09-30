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
package com.alexkudlick.authn.api.entities;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;
import java.util.function.Consumer;
import java.io.Serializable;

@Entity
@Table( name = "users" )
public class UserEntity implements Serializable {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long id;

    @Column( name = "username", nullable = false, unique = true )
    private String userName;

    @Column( name = "encrypted_password", nullable = false, length = 1024 )
    private String encryptedPassword;

    private UserEntity( ) {

    }

    public static void withUnsavedInstance( String userName, String encryptedPassword, Consumer< UserEntity > consumer ) {
        UserEntity entity = new UserEntity( );
        entity.userName = Objects.requireNonNull( userName );
        entity.encryptedPassword = Objects.requireNonNull( encryptedPassword );
        consumer.accept( entity );
    }

    public long getId( ) {
        return id;
    }

    public String getUserName( ) {
        return userName;
    }

    public String getEncryptedPassword( ) {
        return encryptedPassword;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( !( o instanceof UserEntity ) ) return false;
        UserEntity that = ( UserEntity ) o;
        return id == that.id &&
                Objects.equals( userName, that.userName ) &&
                Objects.equals( encryptedPassword, that.encryptedPassword );
    }

    @Override
    public int hashCode( ) {
        return Objects.hash( id, userName, encryptedPassword );
    }

    @Override
    public String toString( ) {
        return "UserEntity{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", encryptedPassword='" + encryptedPassword + '\'' +
                '}';
    }
}
