package org.sobotics.heatdetector;

import javax.annotation.PostConstruct;

import org.sobotics.heatdetector.classify.ModelHandler;
import org.sobotics.heatdetector.domain.DomainHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationSetup {
	

	@Value("${app.domain.folder}")
	private String domainFolder;

	@PostConstruct
	public void init() {
		DomainHandler.initInstance(domainFolder);
		ModelHandler.getInstance();
	}
}
