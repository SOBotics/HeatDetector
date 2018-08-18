package org.sobotics.heatdetector.rest.domain;

import org.sobotics.heatdetector.domain.DomainHandler;
import org.sobotics.heatdetector.domain.file.TextFile;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class DomainController {
	
	@GetMapping("heatdetector/api/domains/**")
	public List<String> getDomains(final HttpServletRequest request) {
		//TODO: Show only domains that users have access to
		return DomainHandler.getInstance().getDomainNames();
	}
	
	@GetMapping("heatdetector/api/regex/{domain}/**")
	public TextFile getRegexen(final HttpServletRequest request, @PathVariable("domain") String domain, @RequestParam(value = "type", required = true) int type) {
		return DomainHandler.getInstance().getDomain(domain).getRegexen(type).getFile();
	}
	
	@PostMapping("heatdetector/api/regex/{domain}/**")
	public void addRegex(final HttpServletRequest request, @PathVariable("domain") String domain, @RequestParam(value = "type", required = true) int type, @RequestParam(value = "regex", required = true) String regex, @RequestParam(value = "index", required = false) Integer index) {
		TextFile file = DomainHandler.getInstance().getDomain(domain).getRegexen(type).getFile();
		if (index != null)
			file.addLine(index, regex);
		else
			file.addLine(regex);
	}
	
	@PutMapping("heatdetector/api/regex/{domain}/**")
	public void editRegex(final HttpServletRequest request, @PathVariable("domain") String domain, @RequestParam(value = "type", required = true) int type, @RequestParam(value = "regex", required = true) String regex, @RequestParam(value = "index", required = true) int index) {
		TextFile file = DomainHandler.getInstance().getDomain(domain).getRegexen(type).getFile();
		file.editLine(index, regex);
	}
	
	@DeleteMapping("heatdetector/api/regex/{domain}/**")
	public void deleteRegex(final HttpServletRequest request, @PathVariable("domain") String domain, @RequestParam(value = "type", required = true) int type, @RequestParam(value = "index", required = true) int index) {
		TextFile file = DomainHandler.getInstance().getDomain(domain).getRegexen(type).getFile();
		file.deleteLine(index);
	}
	
}
