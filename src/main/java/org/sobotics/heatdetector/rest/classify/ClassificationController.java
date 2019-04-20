package org.sobotics.heatdetector.rest.classify;

import org.sobotics.heatdetector.classify.model.ClassifyRequest;
import org.sobotics.heatdetector.classify.model.ClassifyResponse;
import org.sobotics.heatdetector.rest.security.ApiRateLimiter;
import org.sobotics.heatdetector.rest.security.JwtApiKeyUtil;
import org.sobotics.heatdetector.rest.security.RateLimitException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controller that classifiy the content
 * @author Petter Friberg
 *
 */
@RestController
public class ClassificationController {
	
	private final ApiRateLimiter rateLimiter;

	private final JwtApiKeyUtil jwtUtil;

	private final HeatClassifier classifier;

	public ClassificationController(ApiRateLimiter rateLimiter, JwtApiKeyUtil jwtUtil, HeatClassifier classifier) {
		this.rateLimiter = rateLimiter;
		this.jwtUtil = jwtUtil;
		this.classifier = classifier;
	}

	@PostMapping("heatdetector/api/classify/**")
	public ResponseEntity<Object> classify(final HttpServletRequest request, @RequestBody ClassifyRequest classifyRequest, final HttpServletResponse servletResponse) throws RateLimitException, IOException {
		String token = jwtUtil.getToken(request);
		String allowedDomain = jwtUtil.getDomainFromToken(token);
		int maxComments = jwtUtil.getMaxCommentsFromToken(token);
		
		if (!"all".equals(classifyRequest.getDomain()) && !allowedDomain.equals(classifyRequest.getDomain())) {
			servletResponse.sendError(403, "Domain access denied!"); //Looks like it's doubled but is supposed to be exactly that.
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		//Max comments check
		if (maxComments < classifyRequest.getContents().size()) {
//				402 - Payment required. Please upgrade your API key by paying 10$ to this bank account.
//						You can have more than 2 comments per 30 seconds for only 9.99$ a month!!!
			servletResponse.sendError(413, "Above comment limit! Limit: " + maxComments);
			return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).build();
		}
		
		
		rateLimiter.checkLimit(request.getRemoteAddr());
		ClassifyResponse response = classifier.classify(classifyRequest);
		response.setBackOff(rateLimiter.getBackOff(request));
		return ResponseEntity.ok(response);
	}
	
	
}
