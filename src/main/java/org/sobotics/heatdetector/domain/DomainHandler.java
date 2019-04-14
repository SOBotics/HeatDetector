package org.sobotics.heatdetector.domain;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class (instance) loads all domains with it's regex.
 * allowing to check content for regex hit
 * @author Petter Friberg
 *
 */
public class DomainHandler {
	

	private static DomainHandler instance;

	private String domainFolder;

	private Map<String, Domain> domains;

	private DomainHandler(String domainFolder) {
		super();
		this.domainFolder = domainFolder;
		init();
	}

	public static void initInstance(String domainFolder ){
		if (instance!=null){
			throw new RuntimeException("The instance has already been instance");
		}
		instance = new DomainHandler(domainFolder);
	}

	public static DomainHandler getInstance() {
		if (instance == null) {
			throw new NullPointerException("The instance has not been instance correctly, see initInstance");
		}
		return instance;
	}
	
	private void init() {
		domains = new HashMap<>();
		File df = new File(domainFolder);
		if (!df.exists()||!df.isDirectory()){
			throw new RuntimeException("The domain folder: " + domainFolder + " does not exists");
		}
		File[] dsList = df.listFiles(File::isDirectory);

		if (dsList == null) {
			throw new RuntimeException("Issue in reading domains: dsList is null");
		}

		for (File d : dsList) {
			domains.put(d.getName(),new Domain(d));
		}
	}

	/**
	 * Check high, medium and low bad (black list) patterns in this order, if hit return value
	 * 
	 * @param domain,
	 *            name of domain for regexen
	 * @param text,
	 *            already preprocessed
	 * @return <code>null</code> if no hit
	 */
	public RegexHit getRegexBlack(String domain, String text) {
		Domain d = getDomain(domain);

		String hit = d.getHigh().getRegexHit(text);
		if (hit != null) {
			return new RegexHit(hit, Regexen.TYPE_HIGH);
		}
		hit = d.getMedium().getRegexHit(text);
		if (hit != null) {
			return new RegexHit(hit, Regexen.TYPE_MEDIUM);
		}
		hit = d.getLow().getRegexHit(text);
		if (hit != null) {
			return new RegexHit(hit, Regexen.TYPE_LOW);
		}

		return null;
	}


	/**
	 * Check white list patterns, if hit return value else null
	 * 
	 * @param domain,
	 *            name of domain for regexen
	 * @param text,
	 *            already preprocessed
	 * @return <code>null</code> if no hit
	 */
	public RegexHit getRegexWhite(String domain, String text) {
		Domain d = getDomain(domain);

		String hit = d.getWhitelist().getRegexHit(text);
		if (hit != null) {
			return new RegexHit(hit, Regexen.TYPE_WHITELIST);
		}
		return null;
	}


	/**
	 * Check tracking patterns, if hit return value else null
	 * 
	 * @param domain,
	 *            name of domain for regexen
	 * @param text,
	 *            already preprocessed
	 * @return <code>null</code> if no hit
	 */
	public RegexHit getRegexTracking(String domain, String text) {
		Domain d = getDomain(domain);

		String hit = d.getTrack().getRegexHit(text);
		if (hit != null) {
			return new RegexHit(hit, Regexen.TYPE_TRACK);
		}
		return null;
	}
	
	/**
	 * Get a domain
	 * @param domain, name of domain
	 * @return <code>Domain</code> with it's regexen
	 * @throws NoSuchDomainExeception if not present
	 */
	public Domain getDomain(String domain) {
		Domain d = domains.get(domain);
		if (d == null) {
			throw new NoSuchDomainExeception(domain);
		}
		return d;
	}

	public List<Domain> getDomains() {
		return domains.values().stream().sorted().collect(Collectors.toList());
	}
	
	public List<String> getDomainNames() {
		return domains.keySet().stream().sorted().collect(Collectors.toList());
	}

}
