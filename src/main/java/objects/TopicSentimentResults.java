package objects;

/**
 * @author Administrator
 *
 *         A class to display statistics about the topic search though the
 *         comments and a small representation where the topic was mentioned.
 * 
 */
public class TopicSentimentResults {

	private String topic;
	private int topicMentions; // number of time the topic was mentioned in the comments
	private float positiveSentimentScore;
	private float negativeSentimentScore;
	// some reviews to concretely display to our users
	private ReviewInfo[] representativeReviews = new ReviewInfo[3];

	public TopicSentimentResults() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TopicSentimentResults(String topic, int topicMentions, float positiveSentimentScore,
			float negativeSentimentScore, ReviewInfo[] representativeReviews) {
		super();
		this.topic = topic;
		this.topicMentions = topicMentions;
		this.positiveSentimentScore = positiveSentimentScore;
		this.negativeSentimentScore = negativeSentimentScore;
		this.representativeReviews = representativeReviews;
	}

	public float getTotalSentimentPoints() {
		return positiveSentimentScore - negativeSentimentScore;
	}

	public float getAverageSentimentPoints() {
		return (positiveSentimentScore - negativeSentimentScore) / topicMentions;
	}

	public int getTopicMentions() {
		return topicMentions;
	}

	public void countTopicMentions() {
		topicMentions++;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public float getPositiveSentimentScore() {
		return positiveSentimentScore;
	}

	public void addToPositiveSentimentScore(float positiveSentimentScore) {
		this.positiveSentimentScore += positiveSentimentScore;
	}

	public float getNegativeSentimentScore() {
		return negativeSentimentScore;
	}

	public void addToNegativeSentimentScore(float negativeSentimentScore) {
		this.negativeSentimentScore += negativeSentimentScore;
	}

	/*
	 * Add reviewInfo object to represent the hotel topic search in the review
	 * comments. Keep the ones with the shortest sentence
	 */
	public void addRepresentativeReview(ReviewInfo tempReview) {
		if (tempReview == null)
			return;
		if (representativeReviews[0] == null)
			representativeReviews[0] = tempReview;
		else if (representativeReviews[1] == null)
			representativeReviews[1] = tempReview;
		else if (representativeReviews[2] == null)
			representativeReviews[2] = tempReview;
		else {
			if (tempReview.getSelectedSentence().length() < representativeReviews[0].getSelectedSentence().length()) {
				representativeReviews[2] = representativeReviews[1];
				representativeReviews[1] = representativeReviews[0];
				representativeReviews[0] = tempReview;
			} else if (tempReview.getSelectedSentence().length() < representativeReviews[1].getSelectedSentence()
					.length()) {
				representativeReviews[2] = representativeReviews[1];
				representativeReviews[1] = tempReview;
			} else if (tempReview.getSelectedSentence().length() < representativeReviews[2].getSelectedSentence()
					.length()) {
				representativeReviews[2] = tempReview;
			}
		}
	}

	public ReviewInfo[] getRepresentativeReviews() {
		return representativeReviews;
	}

	
	public void printExample(){
		System.out.printf("\t topic: %s, mentioned: %d, positive: %.3f, negative: %.3f, total: %.3f, average: %.3f \n",topic, topicMentions,positiveSentimentScore,negativeSentimentScore,getTotalSentimentPoints(),getAverageSentimentPoints());
		for(int i=0;i<3;i++){
			if(representativeReviews[i]!=null){
				System.out.printf("%d) %s\n",i,representativeReviews[i].getSelectedSentence());
			}
		}
		System.out.println();
//		System.out.printf("\t examples:\n %s \n %s \n %s \n \n",representativeReviews[0].getSelectedSentence(),representativeReviews[1].getSelectedSentence(),representativeReviews[2].getSelectedSentence());
	}
}
