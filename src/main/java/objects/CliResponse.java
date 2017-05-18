package objects;

public class CliResponse {
	private String dir=null;
	private String topic=null;
	private boolean hotlInfo = false;
	private boolean reviewDetails = false;
	private boolean representativeSentences = false;
	private boolean ratings = false;

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public boolean isHotlInfo() {
		return hotlInfo;
	}

	public void setHotlInfo(boolean hotlInfo) {
		this.hotlInfo = hotlInfo;
	}

	

	public boolean isReviewDetails() {
		return reviewDetails;
	}

	public void setReviewDetails(boolean reviewDetails) {
		this.reviewDetails = reviewDetails;
	}

	public boolean isRepresentativeSentences() {
		return representativeSentences;
	}

	public void setRepresentativeSentences(boolean representativeSentences) {
		this.representativeSentences = representativeSentences;
	}

	public boolean isRatings() {
		return ratings;
	}

	public void setRatings(boolean ratings) {
		this.ratings = ratings;
	}

}
