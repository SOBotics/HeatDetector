package org.sobotics.heatdetector.rest.feed;

import org.sobotics.heatdetector.feed.FeedItem;
import org.sobotics.heatdetector.feed.FeedManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@RestController
public class FeedController {

	private final FeedManager thingy;

	public FeedController(FeedManager thingy) {
		this.thingy = thingy;
	}

	@GetMapping({"heatdetector/api/feed/all", "heatdetector/api/feed/all/{domain}"})
	public Collection<FeedItem> getFeedForDomain(@PathVariable Optional<String> domain) {
		if (domain.isPresent()) {
			return thingy.getAllFeedForDomain(domain.get());
		} else {
			return thingy.getAllFeed();
		}
	}

	@GetMapping({"heatdetector/api/feed/bad", "heatdetector/api/feed/bad/{domain}"})
	public Collection<FeedItem> getBadFeed(@PathVariable Optional<String> domain) {
		if (domain.isPresent()) {
			return thingy.getBadFeedForDomain(domain.get());
		} else {
			return thingy.getBadFeed();
		}
	}

}
