package org.sobotics.heatdetector.rest.security.filters;

import org.sobotics.heatdetector.rest.security.JwtApiKeyUtil;
import org.sobotics.heatdetector.rest.security.Permissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Class that contains a filter for the regex section of the webservice.
 * Checks if the the user is allowed to access the domain at all and if so, if the user has correct permission to use the request method.
 *
 * If either of those checks failed a 403 is sent with an explanation.
 */
@Component
@Order(2)
public class RegexAccessFilter extends OncePerRequestFilter {
	
	private JwtApiKeyUtil jwtUtil; //Add header key "Bearer" for login util
	
	public RegexAccessFilter(@Autowired JwtApiKeyUtil jwtUtil){
		this.jwtUtil = jwtUtil;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		final String prefix = "/heatdetector/api/";
		String uri = request.getRequestURI();

		if (!uri.startsWith(prefix)) {
			chain.doFilter(request, response);
			return;
		}

		String relevant = uri.substring(prefix.length());
		String primaryPath = relevant.split("/")[0];
		
		if (primaryPath.equals("regex")) {
			String requestedDomain = relevant.split("/")[1];
			
			String token = jwtUtil.getToken(request);
			String allowedDomain = jwtUtil.getDomainFromToken(token);
			
			//Domain check
			if (!"all".equals(allowedDomain) && !allowedDomain.equals(requestedDomain)) {
				response.sendError(403, "Domain access denied!");
				return;
			}
			
			//Permission check
			int permissionFromToken = jwtUtil.getPermissionFromToken(token);
			int required = Permissions.requiredPermission(request.getMethod());
			
			if (!Permissions.hasPermission(permissionFromToken, required)) {
				response.sendError(403, "Method access denied!");
				return;
			}
		}
		
		chain.doFilter(request, response);
	}
	
	
	
}
