package org.sobotics.heatdetector.classify;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.jsoup.Jsoup;

/**
 * Pre processer
 * Use to pre process content before classification and/or regex
 * According to http://www.cs.cmu.edu/~lingwang/papers/sp250-xiang.pdf, page 2
 * 1. Removed non-English? (using LingPipe [1] with Hadoop). 
 * 2. Remove the shortened URLs. 
 * 3. Remove @username 
 * 4. In tweets remove removed all hashtags from the tweets 
 * 5. To tackle the problem of intentional repetitions, we designed a heuristic to condense 3 or more than 3
 * repetitive letters into a single letter, e.g., hhhheeeello to hello. 
 * 6. For sequences of 2 repetitive letters, we counted how many such sequences
 * each word in a tweet has, and condensed each such sequence into a single
 * letter if the number of such sequences is over a threshold1 For example,
 * yyeeaahh will be reduced to yeah, while committee remains intact. 
 * 7. We removed all stopwords (no need). 
 * 8. We defined a word to be a sequence of letters,- or �, and removed all tokens not satisfying this requirement.
 * 
 * SO related
 * 
 * 9. Remove code 10. Remove html
 *  
 * @author Petter Friberg
 *
 */
public class PreProcesser {
	
	
	private PreProcesser(){
		super();
	}


	/**
	 * Pre process for classification
	 * @param comment
	 * @param tweet
	 * @return
	 */
	public static String preProcessClassification(String comment, boolean tweet) {
		// 1.
		if (comment == null || isNonEnglish(comment)) {
			return null;
		}
		String result = comment;

		// 9. remove <code>sys</code>, for now code blocks does not seem to contain heat..
		result = removeCodeBlocks(result);

		// 10 remove html
		result = removeHtml(result);

		// 3 Remove username
		result = removeUserNames(result);

		// 2 Remove the shortened URLs (URL's in general)
		result = removeUrls(result);

		// 4. Remove hashtags
		result = removeHastags(result);

		if (tweet) {
			// Remove RT
			result = result.replaceAll("RT", "");
		}

		// 5-6. Remove intentional repetitions
		result = removeIntentionalRepetitions(result);

		// 8. Remove chars not in sentance
		result = removeNonSentanceChars(result);

		// Remove all double space or more
		result = removeDoubleSpaces(result);

		// Upper case what do we do with this (seems to give better result in
		// weka)
		result = result.toLowerCase();
		// Repeated words?

		return result;
	}

	/**
	 * Preprocess for regex
	 * @param comment
	 * @return
	 */
	public static String preProcessRegex(String comment) {
		String result = comment;

		// 1 Remove username
		result = removeUserNames(result);

		// 10 remove html
		result = removeHtml(result);

		// 5-6. Remove intentional repetitions
		result = removeIntentionalRepetitions(result);

		// Remove all double space or more
		result = removeDoubleSpaces(result);

		return result;
	}

	public static String removeDoubleSpaces(String result) {
		return result.replaceAll("[ ]{2,}", " ").trim();
	}

	public static String removeNonSentanceChars(String result) {
		// Valid chars are a-zA-z '’ -
		return result.replaceAll("[^a-zA-Z'’,.?!\\- \r\n]", "");
	}

	public static String removeIntentionalRepetitions(String result) {
		// 1 first if 3 letter or more
		// TODO Improve implementation
		// 2 if word contains 2 letters in sequenze and is over threshold
		//probably also if space or puncture between single letters like F U C K and F.U.C.K
		return result.replaceAll("(.)\\1{2,}", "$1");
	}

	public static String removeHastags(String result) {
		// Remove only the hastag for now, maybe if tweet remove also text
		return result.replaceAll("#", "");
	}

	public static String removeUrls(String result) {
		return result.replaceAll("((https?|ftp|gopher|telnet|file|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)", "");
	}

	public static String removeUserNames(String result) {
		return result.replaceAll("@(\\S+)?", "");
	}

	public static String removeHtml(String result) {
		return Jsoup.parse(result).text();
	}

	private static boolean isNonEnglish(String comment) {
		// TODO Implement (can also be used to notify on non english comments)
		return false;
	}

	private static String removeCodeBlocks(String comment) {
		return comment.replaceAll("<code>(.+?)</code>", "");
	}

	/**
	 * Just a test main
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws IOException, InterruptedException {

		System.out.println(removeIntentionalRepetitions("Teeeest thissssssss"));

		// testComments("dev/model_comments_good.txt",2000)

		testComments("dev/model_comments_bad.txt", 2000);

		// testTweets("dev/twitter-hate-speech-processed.csv",2000)
	}

	private static void testComments(String fileName, long readTime) throws IOException, InterruptedException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		try {
			String line = br.readLine();
			while (line != null) {
				System.out.println(line);
				System.out.println(preProcessClassification(line, false));
				System.out.println();
				line = br.readLine();
				Thread.sleep(readTime);
			}
		} finally {
			br.close();
		}
	}

//	public static void testTweets(String fileName, long readTime) throws IOException, InterruptedException {
//		CSVParser parser = CSVParser.parse(new File(fileName), Charset.forName("Cp1252"), CSVFormat.DEFAULT);
//		for (CSVRecord r : parser) {
//			String classif = r.get(0);
//			if (classif.equalsIgnoreCase("The tweet is not offensive")) {
//				continue;
//			}
//			String line = r.get(2);
//			System.out.println(line);
//			System.out.println(preProcessComment(line, true));
//			System.out.println();
//			Thread.sleep(readTime);
//		}
//
//	}

}
