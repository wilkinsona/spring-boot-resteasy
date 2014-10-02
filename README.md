This project provides RESTEasy auto-configuration for a Spring Boot application.

# Getting started

Build this project and install it into your Maven repository:

```
$ mvn install
```

You should then add a dependency on `org.springframework.boot:spring-boot-resteasy:1.0.0.BUILD-SNAPSHOT`
in your application's `build.gradle` or `pom.xml`.

Now create a Spring Boot application with auto-configuration and component scanning enabled:

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@EnableAutoConfiguration
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
```

Next, create a REST endpoint Spring bean annotated using JAX-RS. For example:

```java
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.springframework.stereotype.Component;

@Component
@Path("/hello")
public class Endpoint {

	private String msg;

	@GET
	public String message() {
		return "Hello " + msg;
	}

}
```

That's it! Since `Endpoint` is a Spring `@Component` its lifecycle is managed by Spring and you can
use `@Autowired` dependencies and inject external configuration with `@Value`. Refer to
`spring-boot-sample-resteasy` for a detailed example. It's an executable jar that can be built with
`mvn package` and run with `java -jar`.

# Limitations

RESTEasy [requires any `@Bean` methods on `@Configuration` classes to be public][1]. If RESTEasy
encounters a non-public `@Bean` method you will see an `IllegalStateException` thrown by
`org.jboss.resteasy.plugins.spring.SpringBeanProcessor`.

[1]: https://github.com/resteasy/Resteasy/pull/578