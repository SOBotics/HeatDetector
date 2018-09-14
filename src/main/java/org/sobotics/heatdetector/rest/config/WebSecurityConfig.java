package org.sobotics.heatdetector.rest.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sobotics.heatdetector.rest.security.JwtAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;


/**
 * 
 * TODO: This configure springs authorizzation system, we will use email auth with password and then jwt token
 *
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
	
    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

//    @Autowired
//    private UserDetailsService userDetailsService;
    
    
    
//    @Value("${jwt.route.authentication.path}")
//    private String authUrl;

//    @Autowired
//    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//        
//    	logger.warn("configureAuthentication - START");
//    	authenticationManagerBuilder
//                .userDetailsService(this.userDetailsService)
//                .passwordEncoder(passwordEncoder());
//    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//    	return new BCryptPasswordEncoder(11);
//    }

//    @Bean
//    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() {
//        return new JwtAuthenticationTokenFilter();
//    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
    	logger.info("configure - START");
    	
        httpSecurity
                // token should be ok, so no need for CSRF
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()

                // no sessions
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/**").permitAll() //TODO implement security,  domain (check domain) and admin, apikey permit all and check key in call
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().authenticated();

        // JWT based security filter
        //httpSecurity.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

        // disable caching
        httpSecurity.headers().cacheControl();
        logger.info("configure - END");
    	
    }
    

}