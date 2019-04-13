package org.sobotics.heatdetector.rest.domain;

import org.sobotics.heatdetector.domain.DomainHandler;
import org.sobotics.heatdetector.domain.file.TextFile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Component
@Order(3)
public class ParameterValidationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
		String prefix = "/heatdetector/api/regex/";
		String uri = httpServletRequest.getRequestURI();
		if (!uri.startsWith(prefix)) {
			filterChain.doFilter(httpServletRequest, httpServletResponse);
			return;
		}

		String domain = uri.substring(prefix.length());

		String method = httpServletRequest.getMethod();
		String regex = httpServletRequest.getParameter("regex");

		switch(method.toUpperCase()) {
			case "POST":
			case "PUT":
				if (isNotValidRegex(regex)) {
					httpServletResponse.sendError(400, "\"" + regex + "\" is not a valid regex!");
					return;
				}
			case "DELETE":
				int type = Integer.parseInt(httpServletRequest.getParameter("type"));
				String indexParam = httpServletRequest.getParameter("index");
				int index = indexParam == null ? -1 : Integer.parseInt(indexParam);

				if (isMissingParameters(httpServletRequest)) {
					//If there is anything missing, forward it in the filterchain to let Spring generate potential failure messages
					filterChain.doFilter(httpServletRequest, httpServletResponse);
					return;
				}
				if (indexParam != null && isOutOfBounds(domain, type, index)) {
					httpServletResponse.sendError(400, index + " is out of bounds!");
					return;
				}
		}

		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}

	private boolean isMissingParameters(HttpServletRequest request) {
		String[] postParams = {"type", "regex"};
		String[] putParams = {"type", "regex", "index"};
		String[] deleteParams = {"type", "index"};

		String[] chosen;
		switch(request.getMethod()) {
			case "POST": chosen = postParams; break;
			case "PUT": chosen = putParams; break;
			case "DELETE": chosen = deleteParams; break;
			default: return false;
		}

		for (String p : chosen) {
			if (request.getParameter(p) == null) return true;
		}

		return false;
	}

	private boolean isNotValidRegex(String testSubject) {
		if (testSubject == null) return true;

		try {
			Pattern.compile(testSubject);
			return false;
		} catch (PatternSyntaxException pse) {
			return true;
		}
	}

	private boolean isOutOfBounds(String domain, int type, int index) {
		TextFile file = DomainHandler.getInstance().getDomain(domain).getRegexen(type).getFile();
		return index > file.getLines().size() || index < 1;
	}

}
