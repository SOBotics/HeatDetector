package org.sobotics.heatdetector.rest.domain;

import org.sobotics.heatdetector.domain.DomainHandler;
import org.sobotics.heatdetector.domain.file.TextFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

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
	public ResponseEntity<String> addRegex(final HttpServletRequest request, @PathVariable("domain") String domain, @RequestParam(value = "type", required = true) int type, @RequestParam(value = "regex", required = true) String regex, @RequestParam(value = "index", required = false) Integer index) {
		if (isNotValidRegex(regex)) {
			return new ResponseEntity<>("\"" +regex + "\" is not a valid regex!", HttpStatus.BAD_REQUEST);
		}
		
		TextFile file = DomainHandler.getInstance().getDomain(domain).getRegexen(type).getFile();
		if (index != null) {
			if (index > file.getLines().size() || index < 1) {
				return new ResponseEntity<>(index + " is out of bounds!", HttpStatus.BAD_REQUEST);
			}
			file.addLine(index, regex);
		}
		else
			file.addLine(regex);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PutMapping("heatdetector/api/regex/{domain}/**")
	public ResponseEntity<String> editRegex(final HttpServletRequest request, @PathVariable("domain") String domain, @RequestParam(value = "type", required = true) int type, @RequestParam(value = "regex", required = true) String regex, @RequestParam(value = "index", required = true) int index) {
		if (isNotValidRegex(regex)) {
			return new ResponseEntity<>("\"" + regex + "\" is not a valid regex!", HttpStatus.BAD_REQUEST);
		}
		
		TextFile file = DomainHandler.getInstance().getDomain(domain).getRegexen(type).getFile();
		if (index > file.getLines().size() || index < 1) {
			return new ResponseEntity<>(index + " is out of bounds!", HttpStatus.BAD_REQUEST);
		}
		
		file.editLine(index, regex);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@DeleteMapping("heatdetector/api/regex/{domain}/**")
	public ResponseEntity<String> deleteRegex(final HttpServletRequest request, @PathVariable("domain") String domain, @RequestParam(value = "type", required = true) int type, @RequestParam(value = "index", required = true) int index) {
		TextFile file = DomainHandler.getInstance().getDomain(domain).getRegexen(type).getFile();
		if (index > file.getLines().size() || index < 1) {
			return new ResponseEntity<>(index + " is out of bounds!", HttpStatus.BAD_REQUEST);
		}
		
		
		file.deleteLine(index);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	private boolean isNotValidRegex(String testSubject) {
		try {
			Pattern.compile(testSubject);
			return false;
		} catch (PatternSyntaxException pse) {
			return true;
		}
	}
	
}
