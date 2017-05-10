package operations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.stemmer.PorterStemmer;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import utils.Commons;
import utils.GlobalVar;

/**
 * @author Administrator
 * 
 *         Search through the comment by breaking it down to sentences and in
 *         each one of them look for the topic in hand and the semantic words
 *         that it may be associated with.
 */
public class TopicSearchEvaluation {

	private float commentPositiveSentiment = 0;
	private float commentNegativeSentiment = 0;
	private boolean foundFlag = false; // if the topic is mentioned in this
										// comment
	private String selectedSentence;
	private String[] tokens;

	public void perSentenceSearch(String comment, String topic) throws IOException {
		foundFlag = false;
		for (String sentence : Commons.getSentenceDetectorME().sentDetect(comment)) {
			if (senitmentFinder(sentence, topic))
				foundFlag = true;// if it was found once then is always true
			if (selectedSentence == null || selectedSentence == "" || sentence.length() < selectedSentence.length())
				selectedSentence = sentence;

		}
	}

	/* Evaluate the sentiment for the search-topic */
	public boolean senitmentFinder(String sent, String topic) throws IOException {
		WhitespaceTokenizer st = WhitespaceTokenizer.INSTANCE;
		PorterStemmer ps = new PorterStemmer();

		boolean foundTopic = false;
		String word;
		tokens = st.tokenize(sent);
		List<Integer> topicIdxes = new ArrayList<>();
		List<Integer> nounIndex = null;
		topic = ps.stem(topic);// stem the topic

		for (int i = 0; i < tokens.length; i++) {
			word = ps.stem(tokens[i]);
			if (topic.equalsIgnoreCase(word)) {
				topicIdxes.add(i);
				foundTopic = true;
				if (nounIndex == null)
					nounIndex = sentenceNounExtraction(sent);
				fuzzySemantics(nounIndex, i);
				i = i + GlobalVar.TOPIC_RANGE; // it was considered in the high
												// range
			}

		} // for
		return foundTopic;
	}

	/*
	 * calculate the sentiment based on what is known untill this point in the
	 * sentence
	 */
	private float positiveSentimentEvaluation(float multiplier, String word, int sentimentIdx, int i) {
		/**
		 * No matter what the condition the multiplier (value of the
		 * intensifier) it will become 0 after a semantic expression has been
		 * encountered in the sentence. **************************************
		 * If the intensifier was more than 2 words away from the positive or
		 * negative semantic word/expression then suppose that it was referring
		 * to another adjective that is not set on our semantic database
		 */
		float sentiment = 0;
		if (multiplier == 0) {
			sentiment = Commons.positive.get(word);
			sentimentIdx = i;
		} else {
			if (i - sentimentIdx > 2) {
				sentiment = Commons.positive.get(word);
			} else {
				sentiment = Commons.positive.get(word) * multiplier;
			}
		}
		return sentiment;
	}

	/*
	 * calculate the sentiment based on what is known until this point in the
	 * sentence
	 */
	private float negativeSentimentEvaluation(float multiplier, String word, int sentimentIdx, int i) {
		/**
		 * No matter what the condition the multiplier (value of the
		 * intensifier) it will become 0 after a semantic expression has been
		 * encountered in the sentence. **************************************
		 * If the intensifier was more than 2 words away from the positive or
		 * negative semantic word/expression then suppose that it was referring
		 * to another adjective that is not set on our semantic database
		 */
		float sentiment = 0;
		if (multiplier == 0) {
			sentiment = Commons.negative.get(word);
			sentimentIdx = i;
		} else {
			if (i - sentimentIdx > 2) {
				sentiment = Commons.negative.get(word);
			} else {
				sentiment = Commons.negative.get(word) * multiplier;
			}
		}
		return sentiment;
	}

	/*
	 * Receives a list of indexes for all the nouns in the sentence, the topics
	 * index in the sentence and the tokens of the sentence in hand. It is used
	 * to assign part or full semantic value to the search-topic.
	 */
	private void fuzzySemantics(List<Integer> nounIndex, int topicIdx) {
		/**
		 * the semantic expression is within the topic range, meaning that it
		 * can be referring to it. The problem is that it can be referring to
		 * another noun in the sentence, and thats what is clarified here. ...
		 * *******************************************************************
		 * cases of syntax : 1) [semantic expression], [some noun], [topic] :
		 * the sentiment in this case is attributed to the noun closer to the
		 * semantic expression. ................................................
		 * 2) [some noun], [semantic expression], [topic] : since we can't be
		 * certain regarding who does the expression refers to, its value is
		 * divided between the random noun and the search-topic.
		 */

		float[] sentimentInRange = new float[3];// [0]-positive,
												// [1]-negative,[2] sentidx
		float[] fuzzySentiment = new float[2];// [0]-positive, [1]-negative
		fuzzySentiment[0] = 0;
		fuzzySentiment[1] = 0;
		float curentPos = 0;
		float curentNeg = 0;
		int sentimentIdx = 0;

		// low range
		sentimentInRange = topicRangeSentiment(topicIdx, topicIdx - GlobalVar.TOPIC_RANGE, topicIdx);
		curentPos = sentimentInRange[0];
		curentNeg = sentimentInRange[1];
		sentimentIdx = (int) sentimentInRange[2];
		if (sentimentIdx > -1) {// if a sentiment is found inside topic range
			boolean[] flags = nounInRangeCheck("low", topicIdx, sentimentIdx, nounIndex);
			if (!flags[1])// sentiment refers to other noun
				if (flags[0]) {// split sentiment value
					commentPositiveSentiment += (curentPos / 2);
					commentNegativeSentiment += (curentNeg / 2);
				} else {
					commentPositiveSentiment += curentPos;
					commentNegativeSentiment += curentNeg;
				}

		}

		// high range
		sentimentInRange = topicRangeSentiment(topicIdx, topicIdx, topicIdx + GlobalVar.TOPIC_RANGE);
		curentPos = sentimentInRange[0];
		curentNeg = sentimentInRange[1];
		sentimentIdx = (int) sentimentInRange[2];
		if (sentimentIdx > -1) {// if a sentiment is found inside topic range
			boolean[] flags = nounInRangeCheck("high", topicIdx, sentimentIdx, nounIndex);
			if (!flags[1])// sentiment refers to other noun
				if (flags[0]) {// split sentiment value
					commentPositiveSentiment += (curentPos / 2);
					commentNegativeSentiment += (curentNeg / 2);
				} else {
					commentPositiveSentiment += curentPos;
					commentNegativeSentiment += curentNeg;
				}
		}

	}

	/*
	 * determine whether there is another noun in a certain vicinity with the
	 * semantic expression to which it (the expression)
	 */
	private boolean[] nounInRangeCheck(String orientation, int topicIdx, int sentimentIdx, List<Integer> nounIndex) {
		// [0]-low out-range, [1]-low mid-range, , [2] -high out-range, [3]-high
		// mid-range
		boolean[] sentimentInRange = new boolean[] { false, false, false, false };
		for (Integer nidx : nounIndex) {
			if (orientation.equals("low")) {
				if (nidx >= topicIdx)
					break;
				if (nidx >= sentimentIdx - GlobalVar.TOPIC_RANGE && nidx < sentimentIdx) {
					// case(low out range):[noun] <-topic range-> [sem. expre.]
					// <- topic range-> [topic]
					sentimentInRange[0] = true;
				} else if (nidx < topicIdx && nidx > sentimentIdx) {
					// case(low mid range): [sem. expre.] <- topic -[random
					// noun]- range-> [topic]
					sentimentInRange[1] = true;
					break;
				}
			} else if (orientation.equals("high")) {
				if (nidx > sentimentIdx + GlobalVar.TOPIC_RANGE)
					break;
				if (nidx > sentimentIdx && nidx <= sentimentIdx + GlobalVar.TOPIC_RANGE) {
					// case (high out range): [topic], [sem. exp.], [noun]
					sentimentInRange[2] = true;
				} else if (nidx > topicIdx && nidx < sentimentIdx) {
					// case (high mid range): [topic], [noun], [sem. exp.]
					sentimentInRange[2] = true;
					break;
				}
			}
		} // for

		return sentimentInRange;
	}

	/* Check to see if there is a semantic expression near the search-topic */
	private float[] topicRangeSentiment(int topicIdx, int start, int end) {

		PorterStemmer ps = new PorterStemmer();
		String word;
		float[] sentimentVals = new float[3];// [0]-positive, [1]-negative
		float multiplier = 0;
		float curentPos = 0, curentNeg = 0;
		int sentimentIdx = -1;

		for (int i = start; i < end; i++) {
			word = ps.stem(tokens[i]).toLowerCase();
			if (word.length() <= 2)
				continue;

			if (Commons.intensifiers.containsKey(word)) {
				multiplier += Commons.intensifiers.get(word);
			} else if (Commons.positive.containsKey(word)) {
				curentPos += positiveSentimentEvaluation(multiplier, word, sentimentIdx, i);
				multiplier = 0;
				sentimentIdx = i;
			} else if (Commons.negative.containsKey(word)) {
				curentNeg += negativeSentimentEvaluation(multiplier, word, sentimentIdx, i);
				multiplier = 0;
				sentimentIdx = i;
			}
		} // for
		sentimentVals[0] = curentPos;
		sentimentVals[1] = curentNeg;
		sentimentVals[2] = sentimentIdx;
		return sentimentVals;
	}

	/*
	 * with the use of open nlp POS tager find the nouns in the sentence. They
	 * are considered as possible topics in the sentence.
	 */
	private List<Integer> sentenceNounExtraction(String sent) throws IOException {
		List<Integer> nounIdx = new ArrayList<>();

		String[] wlineTokens = WhitespaceTokenizer.INSTANCE.tokenize(sent);
		String[] tags = Commons.getPOSTaggerME().tag(wlineTokens);
		for (int i = 0; i < tags.length; i++) {
			if (tags[i] == "NN" || tags[i] == "NNS" || tags[i] == "NNP" || tags[i] == "NNPS") {
				nounIdx.add(i);
			}
		}
		return nounIdx;
	}

	public float getCommentPositiveSentiment() {
		return commentPositiveSentiment;
	}

	public void setCommentPositiveSentiment(float commentPositiveSentiment) {
		this.commentPositiveSentiment = commentPositiveSentiment;
	}

	public float getCommentNegativeSentiment() {
		return commentNegativeSentiment;
	}

	public void setCommentNegativeSentiment(float commentNegativeSentiment) {
		this.commentNegativeSentiment = commentNegativeSentiment;
	}

	public boolean isFoundFlag() {
		return foundFlag;
	}

	public void setFoundFlag(boolean foundFlag) {
		this.foundFlag = foundFlag;
	}

	public String getSelectedSentence() {
		return selectedSentence;
	}

	public void setSelectedSentence(String selectedSentence) {
		this.selectedSentence = selectedSentence;
	}

}
