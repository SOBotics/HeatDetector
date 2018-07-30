package org.sobotics.heatdetector;

import javax.annotation.PostConstruct;

import org.sobotics.heatdetector.classify.ModelHandler;
import org.sobotics.heatdetector.domain.DomainHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationSetup {
	

	@Value("${app.folder.domain}")
	private String domainFolder;

	@Value("${app.folder.model}")
	private String modelFolder;
	
	@PostConstruct
	public void init() {
		DomainHandler.initInstance(domainFolder);
		ModelHandler.initInstance(modelFolder);
	}
}
