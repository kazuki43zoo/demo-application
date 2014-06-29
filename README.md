# Demo Application

This is sample application for develop using [TERASOLUNA Global Framework](http://terasoluna.org).<br>
**Currently, this application is under developing !!!**

# Application Overview

This application is developed based [TERASOLUNA Global Framework Development Guideline](http://terasolunaorg.github.io/guideline/).<br>
But it being customized by my opinion in the partially (this means is not 100% compliance by guideline).

## Application Structure

Structure of this application is following.<br><br>
![alt text](./images/application-structure.png "Application Structure")


| Layer | Component/Library | Main responsibilities |
| ----- | --------- | --------------------- |
| Platform             | [Spirng Framework 3.2.9](http://docs.spring.io/spring/docs/3.2.9.RELEASE/spring-framework-reference/htmlsingle/) | Provide mechanism of CDI(Context and Dependency Inject). In this application, use the transaction management and AOP mechanism.|
| Application Layer    | [Spirng Security 3.2.4](http://docs.spring.io/spring-security/site/docs/3.2.4.RELEASE/reference/htmlsingle/) | Provide mechanism of security on web application. |
|                      | [Spirng MVC 3.2.9](http://docs.spring.io/spring/docs/3.2.9.RELEASE/spring-framework-reference/htmlsingle/#mvc) | Coming soon... |
|                      | [Java EE 6 Bean Validation 1.0(JSR-303)](http://beanvalidation.org/1.0/) | Provide mechanism of validation for request data. In this application, use [Hivernate Validator 4.3.1](http://docs.jboss.org/hibernate/validator/4.3/reference/en-US/html/) as implementation provider.|
|                      | Controllers | Handle a request & delegate a business procedure to the services.  |
|                      | DTOs(Forms) | Hold transfer data(form data) of application layer(web layer). |
|                      | JSPs | Generate presentation component(HTML) by accessing to DTOs and Domain Objects.  |
| Domain Layer         | Domain Objects | Hold domain data & implements core business logic. |
|                      | Repositories | Define & provide CRUD operations(interfaces) to the Domain objects. |
|                      | Services | Implements & provide business procedure(Part of the business logic) and provide transaction boundary. |
| Infrastructure Layer | [Mybatis 3.2.7](http://mybatis.github.io/mybatis-3/) | Provide mechanism of database access. In this application, use the Mapper interface as Repository interface. SQL is implements in Mapper MXL. |
| Libraries               | [TERASOLUNA Global Framework Common Library 1.1.0-SNAPSHOT](https://github.com/terasolunaorg/terasoluna-gfw) | Provide useful functionalities on develop enterprise application. And provide dependency on useful OSS libraries([Dozer](http://dozer.sourceforge.net/), [Joda-Time](http://www.joda.org/joda-time/), [Apache-Commons families](http://commons.apache.org/), etc..). |

### Dependency libraries for project-specific
The following libraries is dependency for project-specific without relate on TERASOLUNA Global Framewrok.

| Library | Description |
| ----- | --------------------- |
| [com.h2database:h2:1.4.178](http://www.h2database.com/) |  |
| [cglib:cglib-nodep:2.2.2](https://github.com/cglib/cglib) |  |
| [org.projectlombok:lombok:1.14.2](http://projectlombok.org/) |  |

### Version up of dependency libraries for project-specific  
The following libraries are version up from version that TERASOLUNA Global Framework depend on. Reason of version up is to use the latest version.

| Library | TERASOLUNA Global Framework 1.1.0-SNAPSHOT | In this project |
| ----- | ----- | ----- |
| org.springframework.security | 3.1.4 | 3.2.4 |
| org.springframework.data | 1.6.4 | 1.8.0 |
| org.aspectj | 1.7.3 | 1.8.0 |
| org.codehaus.jackson | 1.9.7 | 1.9.13 |
| org.slf4j | 1.7.5 | 1.7.7 |
| ch.qos.logback | 1.0.13 | 1.1.2 |
| jboss-logging | 3.1.0 | 3.1.4 |
| joda-time.version | 2.2 | 2.3 |
| dozer | 5.4.0 | 5.5.1 |
| com.google.guava | 13.0.1 | 17.0 |
| commons-beanutils | 1.8.3 | 1.9.2 |
| commons-lang | 3.1 | 3.3.2 |
| commons-dbcp | 1.2.2.patch_DBCP264_DBCP372 | 1.4 |


description coming soon...

## Java package Structure

Coming soon...

## Resources package Structure

Coming soon...

## Web resources Structure

Coming soon...

## Application Functionalities
Coming soon...

# Functionality Details

## Authentication
This section describe about authentication in this application.<br>
In this application, authentication(login and logout) processing are implements using Spring Security and Spring MVC.<br>

* Spring Security has responsible for the authentication processing.
* Spring MVC has responsible for screen flow control.

### View the login form

![alt text](./images/flow-view-login-form.png "Flow of view the login form page")

description coming soon...

### Login(Authenticate)

![alt text](./images/flow-authentication.png "Flow of login")

description coming soon...

### Handle login error

![alt text](./images/flow-handle-authentication-error.png "Flow of handle the authentication error")

description coming soon...

### Handle validation error

![alt text](./images/flow-handle-authentication-validation-error.png "Flow of handle the validation error on login")

description coming soon...

### Logout

![alt text](./images/flow-logout.png "Flow of logout")

description coming soon...

### Logout by session timeout
Coming soon...

## Authorization
This section describe about authorization in this application.<br>
In this application, authorization(access control of protected page) processing are implements using Spring Security.<br>

## Other Security Countermeasure
This section describe about other security countermeasure in this application.<br>

### CSRF Protection
Coming soon...

## Session Management
This section describe about session management in this application.<br>

### Detect Session timeout
Coming soon...

