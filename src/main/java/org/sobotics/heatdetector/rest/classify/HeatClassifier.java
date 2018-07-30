package org.sobotics.heatdetector.rest.classify;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sobotics.heatdetector.classify.ModelHandler;
import org.sobotics.heatdetector.classify.PreProcesser;
import org.sobotics.heatdetector.classify.model.ClassifyRequest;
import org.sobotics.heatdetector.classify.model.ClassifyResponse;
import org.sobotics.heatdetector.classify.model.Content;
import org.sobotics.heatdetector.classify.model.Result;
import org.sobotics.heatdetector.domain.DomainHandler;
import org.sobotics.heatdetector.domain.Regexen;

public class HeatClassifier {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HeatClassifier.class);

	
	public static final double HIGH_THRESHOLD = .999d;
	public static final double MEDIUM_THRESHOLD = .995d;
	public static final double LOW_THRESHOLD = .95d;
	
	
	public ClassifyResponse classify(ClassifyRequest request) {
		if (request == null) {
			return null;
		}

		ClassifyResponse response = new ClassifyResponse(request.getDomain());

		List<Result> resultList = new ArrayList<>();
		response.setResult(resultList);

		for (Content c : request.getContents()) {
			Result r = classify(request.getDomain(),c);
			if (r != null && r.getScore() >= request.getMinScore()) {
				resultList.add(r);
			}
		}

		return response;

	}

	public Result classify(String domain, Content content) {
		if (content == null) {
			return null;
		}

		Result r = new Result();

		r.setId(content.getId());

		//Pre process for classification
		String classifyText = PreProcesser.preProcessClassification(content.getText(), false);

		// Weka Naive Bayes
		try {
			r.setNb(ModelHandler.getInstance().classifyMessageNaiveBayes(classifyText)[1]);
		} catch (Exception e) {
			r.setNb(Double.NaN);
			LOGGER.error("Weka faild to classify",e);
		}
		// Open NLP
		try {
			r.setOp(ModelHandler.getInstance().classifyMessageOpenNLP(classifyText)[1]);
		} catch (Exception e) {
			r.setOp(Double.NaN);
			LOGGER.error("Open nlp faild to classify",e);
		}
		
		//Pre process for regex
		classifyText = PreProcesser.preProcessRegex(content.getText());
		
		r.setBad(DomainHandler.getInstance().getRegexBlack(domain, classifyText));
		r.setGood(DomainHandler.getInstance().getRegexWhite(domain, classifyText));
		r.setTrack(DomainHandler.getInstance().getRegexTracking(domain, classifyText));
		
		r.setScore(getScore(r));

		return r;
	}

	private int getScore(Result r) {
		if (r==null){
			return 0;
		}
		
		//Weka 6, Open 4 (we trust NB  more)
		int score = 0;
		score+=getNlpScore(r.getNb(), 6);
		score+=getNlpScore(r.getOp(), 4);
		
		//Regex
		if (r.getBad()!=null){
			switch (r.getBad().getType()){
			case Regexen.TYPE_HIGH:
				score+=6;
				break;
			case Regexen.TYPE_MEDIUM:
				score+=4;
				break;
			case Regexen.TYPE_LOW:
				score+=6;
				break;
			default:
				//nothing
			}
		}
		
		if (r.getGood()!=null){
			score-=6;
		}
		
		return Math.min(score, 10);
	}

	

	private int getNlpScore(double pv, int maxScore) {
		// We trust NB more so 6 score for weka, 4 score for open
		
		if (pv>HIGH_THRESHOLD){
			return maxScore;
		}
		if (pv>MEDIUM_THRESHOLD) {
			return maxScore/2;
		}
		if (pv>LOW_THRESHOLD) {
			return maxScore/4;
		}
		if (pv<1-HIGH_THRESHOLD) {
			return -maxScore;
		}
		if (pv<1-MEDIUM_THRESHOLD) {
			return -maxScore/2;
		}
		
		if (pv<1-LOW_THRESHOLD) {
			return -maxScore/4;
		}
		return 0;
	}
}
