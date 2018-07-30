package org.sobotics.heatdetector.rest.domain;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.sobotics.heatdetector.domain.DomainHandler;
import org.sobotics.heatdetector.domain.file.TextFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DomainController {

	@GetMapping("heatdetector/api/domains/**")
	public List<String> getDomains(final HttpServletRequest request)  {
		//TODO: Show only domains that users have access to
		return DomainHandler.getInstance().getDomainNames();
	}
	
	@GetMapping("heatdetector/api/regex/{domain}/**")
	public TextFile getRegexen(final HttpServletRequest request,@PathVariable("domain") String domain, @RequestParam(value = "type", required = true) int type) {
		return DomainHandler.getInstance().getDomain(domain).getRegexen(type).getFile();
	}
	
}
