package org.sobotics.heatdetector.rest.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

	private final Log logger = LogFactory.getLog(this.getClass());

	//@Autowired
	//private JwtAuthenticateUtil jwtUtil; //Add header key "Bearer" for login util
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

		//TODO: Add autentication
		chain.doFilter(request, response);
	}
	
	public String getFullURL(HttpServletRequest request) {
	    StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
	    String queryString = request.getQueryString();

	    if (queryString == null) {
	        return requestURL.toString();
	    } else {
	        return requestURL.append('?').append(queryString).toString();
	    }
	}
}