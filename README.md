## Build A Modular Spring Boot RESTful API with the ***mrJar*** Gradle Plugin

This project demonstrates the ease with which you can build and run a module-based RESTful API using Spring Boot, Jersey and [*the **mrJar** plugin*](http://bit.ly/mrJar.com). 
The code in this project is a Spring Boot port of [*this Dropwizard-based project*]().
 As well as using a different framework, it uses a different Java module plugin too. 

### Prerequisites

• Java 9<sup>+</sup> <br />
• Gradle 5.0<sup>+</sup> <br />
• A *`REST`* client (*e.g., `curl`*) <br />
• An *`AUTHENTICATION_URL`* environment variable <br /> 

### mrJar — JPMS Modules Made Easy

The Dropwizard-based project had a nifty CLI feature. But for the purposes of demonstrating a Jigsaw module-based RESTful API, a full-blown CLI feature is not strictly necessary. Without it, the application is both much easier to use ***and*** a lot simpler to explain. The only command we really need in the *`authn-api`* module is Gradle's *`:run`* task to start the server. Removing the CLI also [*solved another frustrating problem*](). 

### Zero Database Configuration

Another thing that's a lot simpler in this Spring Boot project compared to the Dropwizard one: *You don't need to futz around with a database to demo this project*. This project simply uses an embedded, in-memory database. It get's automatically recreated and initialized when you *`:run`* the app.
 
### Refactor *`build.gradle`* to Use ***mrJar***

The following refactoring has been implemented to simplify the *`build.gradle`* and streamline the building and running of this project's JPMS modules:

1. Replace the *`com.zyxist.chainsaw`* plugin with the ***mrJar*** plugin
2. Remove the *`com.github.johnrengelman.shadow`* plugin and its associated configuration
3. Spin off the *`authentication-models`* project to a remotely-published binary module
4. Organize the two remaining projects into what Gradle calls a [*Composite Build*](http://bit.ly/CompBlds).
5. Add a *`mrjar{ }`* block to the *authn-client* section of the root project's composite *`build.gradle`*:
    
        mrjar{ 
            main = 'com.alexkudlick.authn.client.AuthenticationClientApplication'
            args = input
        }
    
6. Add a *`mrjar{ }`* block to the *authn-api* section of the root project's composite *`build.gradle`*:
    
        mrjar{ 
            main = 'com.alexkudlick.authn.api.AuthnApplication'
        }
           
### Build the JPMS Modules

This was the easiest, most straightforward part of the whole process. 

1. First, do a sanity check to make sure everything is good-to-go:

       gradlew checkAll
       ...
       BUILD SUCCESSFUL
       ...

2. Then bring 'er on home:

       gradlew assembleAll
       ...
       BUILD SUCCESSFUL
       ...

At this point you have two modular jars in the two *included builds*' respective *`build/libs`* folders. You can confirm ***mrJar's*** successful creation of either of the two explicit modules with:

        jar -f authn-api/build/libs/mr.jar.authn.api-0.0.0.jar --describe-module
        mr.jar.authn.api jar:file:///.../authn-api/build/libs/mr.jar.authn.api-0.0.0.jar/!module-info.class
        ...
        requires com.alexkudlick.authentication.models
        ...
        requires jackson.annotations
        ...
        requires org.hibernate.orm.core
        ...
        requires spring.boot
        ...
        qualified opens com.alexkudlick.authn.api.web to jersey.server


### Start the Server

Make sure you've set the *`DATABASE_JDBC_URL`* environment variable. The authn-api's build script needs to look like this:

    mrjar{ 
        main = 'com.alexkudlick.authn.api.AuthnApplication'
    }
    
Then you're good to run:

    gradlew runApi

### Create a User

Unlike the orginal Dropwizard application, this one removes the need for you to „*migrate*“ a database. Spring Boot automatically creates a *`User`* table for you on startup. But you do need to then run the *`authn-client`* module to create a test *`User`* using some Gradle properties I preconfigured for you.
 
There are another few lines in the build script that I elided from the above snippet. What those lines do is read some pre-populated properties from the client project's *`gradle.properties`* file:

    ...
    option=--create
    user=mister.jar
    pwd=testPassword
    ...
    
Having those preset input values spares you a few keystrokes at the command line. Make sure your build script looks like what I described above:

    
    mrjar{ 
        main = 'com.alexkudlick.authn.client.AuthenticationClientApplication'
        args = input
    }
    

 After that, you need to set the *`AUTHENTICATION_URL`* environment variable to *`http://localhost:8080`*.  Now, *`cd`* into the *authn* directory then run this command to create a user:

    gradlew runClient
       
You should see this output in the console:

    ...
       
    ok - User 'mister.jar' was created
    
    ...

To run the *`login`* command, you can edit the *`gradle.properties`* file to this : 

    ...
    
    option=--login
    user=mister.jar
    pwd=testPassword

    ...

Then run the client again:

    gradlew runClient
       
You should see this output in the console:

    ...
       
    User mister.jar's Authentication token: 379ba3c5-4c70-45ee-9cee-f3782f5fd1aa
    
    ...


To run the *`check`* command, edit *`gradle.properties`* to this: 

    ...
    
    option=--check
    token=379ba3c5-4c70-45ee-9cee-f3782f5fd1aa
    
    ...
    
    gradlew runClient
    
    ...
           
    ok - 379ba3c5-4c70-45ee-9cee-f3782f5fd1aa is a valid token
        
    ...

Instead of putting the input in the properties file, you could pass those same properties in on the command line. Like so:

    ...
    
    gradlew runClient -Poption=--check -Ptoken=379ba3c5-4c70-45ee-9cee-f3782f5fd1aa
    
    ...

Or you could also fire HTTP messages at the server to get the same results:

    $ curl -i -X POST -H "Content-Type: application/json" --data '{"userName": "mister.jar", "password": "testpassword"}' http://localhost:8080/api/tokens/

    ...

    {"token":"379ba3c5-4c70-45ee-9cee-f3782f5fd1aa"}

   
This demonstration showed you how ***mrJar*** makes it easy to organize, build and run a Spring Boot Jersey RESTful API architected with JPMS modules.  If this page does not go into sufficient technology detail for you, then you might find [*the original blog*](http://bit.ly/akudBlog) of some value.

Please get in touch with any questions.