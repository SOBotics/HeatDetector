package org.sobotics.heatdetector.rest.security;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
@Primary
public class JwtApiKeyUtil implements Serializable {

	private static final long serialVersionUID = -3301605591108950415L;

	public static final String CLAIM_KEY_EMAIL = "email";
	public static final String CLAIM_KEY_APPLICATION = "app";
	public static final String CLAIM_KEY_USER_DOMAIN = "domain";
	public static final String CLAIM_KEY_CREATED = "created";

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.header}")
	private String tokenHeader;

	public String getToken(HttpServletRequest request) {
		// Get from request
		String authToken = request.getHeader(this.tokenHeader);

		if (authToken != null && authToken.startsWith("Bearer ")) {
			authToken = authToken.substring(7);
		} else {
			authToken = null;
		}
		return authToken;
	}

	public Date getCreatedDateFromToken(String token) {
		final Claims claims = getClaimsFromToken(token);
		return new Date((Long) claims.get(CLAIM_KEY_CREATED));
	}

	
	public String getEmailFromToken(String token) {
		final Claims claims = getClaimsFromToken(token);
		return (String) claims.get(CLAIM_KEY_EMAIL);
	
	}


	public String getDomainFromToken(String token) {
		final Claims claims = getClaimsFromToken(token);
		return (String) claims.get(CLAIM_KEY_USER_DOMAIN);

	}

	public Claims getClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(getSecret()).parseClaimsJws(token).getBody();
	}

	public String generateToken(String email, String application, String domain) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(CLAIM_KEY_CREATED, new Date());
		claims.put(CLAIM_KEY_EMAIL, email);
		claims.put(CLAIM_KEY_APPLICATION, application);
		claims.put(CLAIM_KEY_USER_DOMAIN, domain);
		return generateToken(claims);
	}

	public String generateToken(Map<String, Object> claims) {
		return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, getSecret()).compact();
	}


	public Boolean validateToken(String token, String domain) {
		try {
			final String rDomain = getDomainFromToken(token);
			return rDomain != null && rDomain.equals(domain);
		} catch (RuntimeException e) {
			return false;
		}
	}

	public String getSecret() {
		return this.secret;
	}

}