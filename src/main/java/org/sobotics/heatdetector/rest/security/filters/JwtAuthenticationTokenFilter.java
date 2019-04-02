package org.sobotics.heatdetector.rest.security.filters;

import io.jsonwebtoken.JwtException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sobotics.heatdetector.rest.security.JwtApiKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	private JwtApiKeyUtil jwtUtil; //Add header key "Bearer" for login util
	
	public JwtAuthenticationTokenFilter(@Autowired JwtApiKeyUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		String token = jwtUtil.getToken(request);
	
		if (token == null) {
			response.sendError(401, "No API key found");
			return;
		}

		try {
			jwtUtil.getClaimsFromToken(token);
		} catch (JwtException jwte) {
			response.sendError(401, "JWT invalid!");
			return;
		}

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