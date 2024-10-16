# Description of the contents of this project

## How to build, test & deploy the code 
This project is using the following technologies: 
- Kotlin 2.0.20 as the main programming language. 
- Gradle 8.5 as the build system. 
- Spring Boot 3.3.3 as a framework that simplifies the development of web applications. It requires a Java version between 17 and 21.

As Gradle is used as the build tool, to build, test & deploy the application you must follow these steps: 
1. Run ```./gradlew build``` to compile the code, run the tests and pack the artifacts. It also verify the dependencies and secures the project is production-ready. 
2. Run ```./gradlew check``` in order to execute all verifications and validations defined in the project. It also runs the tests.
3. Run ```./gradlew bootrun``` to deploy the web application at http://localhost:8080 

You can also run ```./gradlew test``` in order to execute the tests without building the project nor performing aditional verification steps. 

See section 6 to deploy it using Docker, section 7  if you want to use docker-compose and section 8 to use kubernetes (Minikube is recommended).

## What have I added to the project? 
### 1. Time-based Greeting (~1h30min spent on implementation + 12 minutes on documentation)
The greeting message varies depending on the time of day. The variations are as follows:
- 06:00 - 11:59: "Good morning, student!"
- 12:00 - 17:59: "Good afternoon, student!"
- 18:00 - 21:59: "Good evening, student!"
- 22:00 - 05:59: "Good night, student!"

To implement the time-based greeting functionality, the following changes have been made:

1. `src/main/resources/application.properties`: Added properties for different greetings (app.greeting.morning, app.greeting.afternoon, app.greeting.evening & app.greeting.night). 
2. `src/main/kotlin/controller/HelloController.kt`: Declared variables for each greeting. These variables are injected with the values from the `application.properties` file using the ```@Value("\${app.greeting.XXX}")``` tag, where "XXX" is the day time (morning, evening, afternoon or night). Default values are provided for these variables. 
3. `src/main/kotlin/controller/HelloController.kt`: Implemented the `getTimeBasedGreeting()` function, which returns the appropiate greeting based on the current system time. 
4. `src/test/kotlin/HelloControllerMVCTests.kt` and `src/test/kotlin/HelloControllerUnitTests.kt`: Updated tests to reflect the new behavior. Both test files now expect the greeting along with the default message. Note that the Unit Tests do not use Spring, so they rely on the default values of the variables rather than those in `application.properties`. 

I've used GitHub Copilot to assist with some aspects of the code, as this is my first time working with Spring and Kotlin. However, all the core logic has been developed by me. I only sought help for questions related to syntax and notation.

### 2. Theme Customization (~45 minutes spent on implementation + 9 minutes on documentation) 
The user can customize the website's background color and the font style of the displayed text.

The following changes have been made to implement this functionality:
1. `src/main/kotlin/controller/HelloController.kt`: Now `welcome` function expects two additional parameters: backgroundColor & fontStyle, both of which are strings.  If cookies with the same names (backgroundColor and fontStyle) exist, their values are used for these parameters; otherwise, default values are applied (#FFFFFF for backgroundColor and Arial for fontStyle). The model map assigns the values of model["backgroundColor"] and model["fontStyle"] from the cookies (or the default values if the cookies don't exist). This ensures that the HTML template is rendered using those values.
2. `src/main/kotlin/controller/HelloController.kt`: A new function `setTheme` has been implemented. This function is called whenever the user makes a POST request to `/setTheme` (  @PostMapping("/setTheme")). It accepts two parameters, `backgroundColor`and `fontStyle`, both passed as request parameters (@RequestParam). Then, the function creates two cookies: one for `backgroundColor`, storing the user's selected background color; and other for `fontStyle`, which stores the user's selected font style. Both cookies are set to expire after 1 week and are made valid throughout the entire application (`path("/")`). The cookies are then added to the HTTP response headers, and the user get redirected back to the root path ("/") and send the user a 302 HTTP status code (redirection). 
3. `src/main/kotlin/controller/HelloController.kt`: Two stub cookies have been made with the values #FFFFFF and Arial. Then, the stub cookies are passed to the `welcome` function of the controller, and the values are expected to be the default ones (#FFFFFF & Arial respectively). 

### 3. User Authentication (~1h30min spent on implementation + 12 minutes on documentation)
(Reference: https://spring.io/guides/gs/securing-web)

The application now supports user authentication. I've created a list of available users (user, user1, user2, user3, and admin) with some example passwords. In a real project, I would create a database of users and provide a registration option, possibly using JPA or another ORM.

The following changes have been made:

1. `src/main/kotlin/controller/config/WebSecurityConfig.kt`: In this file, I've defined the security chain, which acts like a firewall. This allows me to specify which routes are accessible to which roles. For demonstration purposes, I've made every route accessible to everyone. I've also defined the /login and /logout routes and the actions taken after logging in/out (redirecting to root).

2. `src/main/kotlin/controller/HelloController.kt`: In the `welcome` function, I've added a `userDetails` parameter, which is an AuthenticationPrincipal. This can retrieve the username and password of the authenticated user (if one is authenticated) and create a personalized greeting. I've also created a `login` function which returns the `login.html` template. This function maps the GET request to /login.

3. `build.gradle.kts`: I've added the dependencies for Spring Security.

4. `src/main/resources/templates/login.html`: This is a simple login template.

I've also made it so that if you log in with 'admin' as both the username and password, a hidden button will be displayed.

### 4. REST API Endpoint (7 minutes spent on implementation + 3 minutes on documentation)
(https://spring.io/guides/gs/rest-service used)

The application now has a REST API endpoint at `/api`. I've created a simple example: when the user makes a GET request to `/api/greeting`, they receive a greeting in JSON format.
To achieve this, the following changes have been made:

1. `src/main/kotlin/controller/GreetingController.kt`: Contains a `getGreeting` function that maps GET requests to `/api/greeting`. It returns the greeting message in JSON format.

I tested this by making a GET request to `/api/greeting` and observing the HTTP response using the "ReqBin Client" extension.

### 5. Greeting History (14 minutes spent on implementation + 12 minutes on documentation)

I've introduced a new feature in the REST API endpoint at `/api/greeting/history`. Now, when an administrator sends a GET request to this route, they receive a JSON response containing all the greetings, each with the associated user and timestamp.

To achieve this, the following changes have been made: 

1. `src/main/kotlin/model/Greeting.kt`: Data class which represent a Greeting (user, message and timestamp).
2. `src/main/kotlin/model/GreetingService.kt`: This service has two functions: `addGreeting()`, which adds a new greeting to the list; and `getGreetings()`, which returns the list all greeting. In a production environment, these functions would interact with a database (possibly using JPA) to persist the greeting history. However, for the purposes of this example, maintaining the history in a list is sufficient.
3. `src/main/kotlin/controller/GreetingController.kt`: It now requires the user to be authentificated (if it is not, the default username is 'Guest'). It also calls `addGreeting()` function in order to register the new greeting. It has a new metthod: `getGreetingHistory()`, which maps GET petitions to `/api/greeting/history` and returns a .json with every greeting made. 
4. `src/main/kotlin/controller/config/WebSecurityConfig.kt`: I have aded a restriction rule in order to achieve that only admin can go to `/api/greeting/history`. If the user is not admin, it get redirected to /login. 

### 6. Multi-Lang (60 minutes spent on implementation + 8 minutes on documentation)
I have some technical problems so it is partially implemented. The tech issue is that Spring gets the Locale from the web browser and it ignores options entered by user, so to test it you must change your browse default language in your browser settings. 

I've made a `LocaleConfigurator.kt` class which configures locale settings for a Spring application. It sets the default locale to `Locale.US`, allows changing the locale via a `lang` parameter in the request URL (not working), and loads message resources from `messages.properties`. 

I've created 4 `messages.properties` variants:
1. `messages.properties`, which has the default greeting (prueba)
2. `messages_es.properties`, which has the greeting in Spanish
3. `messages_en.properties`, which has the greeting in English
4. `messages_fr.properties`, which has the greeting in French

I've also modified `HelloController.kt` to use a messageSource, and when `welcome` function is called, it retrieves a localized message with the key "greeting" for the current locale. In `welcome.html` templated, I've included a paragraph with `th:text="#{greeting}"` tag in order to show the message to the user. 

### 7. Docker (16 minutes spent on implementation + 4 minutes on documentation)
I made a simple Dockerfile which creates a Docker image that sets up a Gradle 8.5 environment. It copies the source code of the application into the default Gradle folder (/home/gradle/src) within the Docker container. Then, it runs the gradle build command to compile the application and generate an executable JAR file.

Build the container with: ```docker build -t my-spring-app .``` in the root directory of the project. 
Deploy the webapp on 8080 of your localhost: ```docker run -p 8080:8080 my-spring-app``` in the root directory of the project. 

### 8. Docker Compose (7 minutes spent on implementation + 2 minutes on documentation)
The docker-compose.yml file defines a service my-spring-app that builds a Docker image from the Dockerfile in the current directory and runs it, mapping port 8080 of the host to port 8080 of the container.

Run it with ```docker-compose-up``` in the root directory of the project. 

### 9. Kubernetes (30min spent on implemntation + 5 minutes on documentation)
If you don't have a Kubernetes cluster configured, install Minikube and run each command. Otherwise, push the container to Docker Hub (Docker push) and then apply the `my-spring-app.yml` manifest (command 4).

1. `minikube start`
2. `eval $(minikube docker-env)`
3. `docker build -t my-spring-app:latest .`
4. `kubectl apply -f my-spring-app.yml`

I have also uploaded this image to Docker Hub (839953/my-spring-app)