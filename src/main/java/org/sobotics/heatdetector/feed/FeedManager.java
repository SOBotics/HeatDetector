package org.sobotics.heatdetector.feed;

import org.sobotics.heatdetector.classify.model.Content;
import org.sobotics.heatdetector.domain.Domain;
import org.sobotics.heatdetector.domain.DomainHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.*;

@Component
@ApplicationScope
public class FeedManager {

	private static final int MAX_LENGTH = 50;

	private Queue<FeedItem> badFeed = new LinkedList<>();
	private Queue<FeedItem> allFeed = new LinkedList<>();

	public void addFeedItem(String domainString, Content comment, boolean isBad) {
		Domain domain = DomainHandler.getInstance().getDomain(domainString);

		FeedItem item = new FeedItem(comment.getId(), domain.getName(), comment.getText(), comment.getHref());
		addWithMaxLength(allFeed, item);
		addWithMaxLength(domain.getAllFeed(), item);
		if (isBad) {
			addWithMaxLength(domain.getBadFeed(), item);
			addWithMaxLength(badFeed, item);
		}
	}

	private <T> void addWithMaxLength(Queue<T> q, T item) {
		q.add(item);
		while (q.size() > FeedManager.MAX_LENGTH) {
			q.poll();
		}
	}

	public Collection<FeedItem> getBadFeed() {
		return Collections.unmodifiableCollection(badFeed);
	}

	public Collection<FeedItem> getAllFeed() {
		return Collections.unmodifiableCollection(allFeed);
	}

	public Collection<FeedItem> getAllFeedForDomain(String domain) {
		return Collections.unmodifiableCollection(DomainHandler.getInstance().getDomain(domain).getAllFeed());
	}

	public Collection<FeedItem> getBadFeedForDomain(String domain) {
		return Collections.unmodifiableCollection(DomainHandler.getInstance().getDomain(domain).getBadFeed());
	}

}
