package org.sobotics.heatdetector.rest.classify;

import org.sobotics.heatdetector.classify.model.ClassifyRequest;
import org.sobotics.heatdetector.classify.model.ClassifyResponse;
import org.sobotics.heatdetector.rest.security.ApiRateLimiter;
import org.sobotics.heatdetector.rest.security.JwtApiKeyUtil;
import org.sobotics.heatdetector.rest.security.RateLimitException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller that classifiy the content
 * @author Petter Friberg
 *
 */
@RestController
public class ClassificationController {
	
	private final ApiRateLimiter rateLimiter;

	private final JwtApiKeyUtil jwtUtil;

	public ClassificationController(ApiRateLimiter rateLimiter, JwtApiKeyUtil jwtUtil) {
		this.rateLimiter = rateLimiter;
		this.jwtUtil = jwtUtil;
	}

	@PostMapping("heatdetector/api/classify/**")
	public ResponseEntity<Object> classify(final HttpServletRequest request, @RequestBody ClassifyRequest classifyRequest) throws RateLimitException {
		String token = jwtUtil.getToken(request);
		String allowedDomain = jwtUtil.getDomainFromToken(token);
		int maxComments = jwtUtil.getMaxCommentsFromToken(token);
		
		if (!"all".equals(classifyRequest.getDomain()) && !allowedDomain.equals(classifyRequest.getDomain())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Domain access denied!");
		}
		
		//Max comments check
		if (maxComments < classifyRequest.getContents().size()) {
//				402 - Payment required. Please upgrade your API key by paying 10$ to this bank account.
//						You can have more than 2 comments per 30 seconds for only 9.99$ a month!!!
			return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("Above comment limit! Limit: " + maxComments);
		}
		
		
		rateLimiter.checkLimit(request.getRemoteAddr());
		ClassifyResponse response = new HeatClassifier().classify(classifyRequest);
		response.setBackOff(rateLimiter.getBackOff(request));
		return ResponseEntity.ok(response);
	}
	
	
}
