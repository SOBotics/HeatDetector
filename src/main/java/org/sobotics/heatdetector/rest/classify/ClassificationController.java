package org.sobotics.heatdetector.rest.classify;

import javax.servlet.http.HttpServletRequest;

import org.sobotics.heatdetector.classify.model.ClassifyRequest;
import org.sobotics.heatdetector.classify.model.ClassifyResponse;
import org.sobotics.heatdetector.rest.security.ApiRateLimiter;
import org.sobotics.heatdetector.rest.security.RateLimitException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller that classifiy the content
 * @author Petter Friberg
 *
 */
@RestController
public class ClassificationController {
	
	@Autowired
	private ApiRateLimiter rateLimiter;

	
	@PostMapping("heatdetector/api/classify/**")
	public ClassifyResponse classify(final HttpServletRequest request, @RequestParam(value="api-key", required = false) String apiKey, @RequestBody ClassifyRequest classifyRequest) throws RateLimitException {
		rateLimiter.checkLimit(request.getRemoteAddr());
		ClassifyResponse response = new HeatClassifier().classify(classifyRequest);
		response.setBackOff(rateLimiter.getBackOff(request));
		return response;
	}
	
	
}
