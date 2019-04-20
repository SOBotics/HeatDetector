package org.sobotics.heatdetector.feed;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

public class FeedItem {

	private long id;
	private String domain;
	private String text;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String href;

	public FeedItem(long id, String domain, String text, String href) {
		this.id = id;
		this.domain = domain;
		this.text = text;
		this.href = href;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
}
