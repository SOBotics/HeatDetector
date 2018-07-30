package org.sobotics.heatdetector;

import org.sobotics.heatdetector.rest.config.WebSecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;


@SpringBootApplication
@Import({ WebSecurityConfig.class})
@EnableAutoConfiguration(exclude = { FreeMarkerAutoConfiguration.class })
public class Application extends SpringBootServletInitializer{
	
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) {
    	SpringApplication.run(Application.class, args);
    }
}
