package objects;

import java.time.LocalDate;

/**
 * @author Administrator
 *
 *         To better serve our users show a concrete example of the review
 *         sentence and details regarding that comment
 */
public class ReviewInfo {

	private String selectedSentence;
	private String authName;
	private String authLocation;
	private String reviewTitle;
	private String reviewId;
	private String reviewComment;
	private String date;

	public ReviewInfo() {
		super();
	}

	public ReviewInfo(String selectedSentence, String authName, String authLocation, String reviewTitle,
			String reviewId, String reviewComment, String date) {
		super();
		this.selectedSentence = selectedSentence;
		this.authName = authName;
		this.authLocation = authLocation;
		this.reviewTitle = reviewTitle;
		this.reviewId = reviewId;
		this.reviewComment = reviewComment;
		this.date = date;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date= date;
	}

	public String getAuthName() {
		return authName;
	}

	public void setAuthName(String authName) {
		this.authName = authName;
	}

	public String getAuthLocation() {
		return authLocation;
	}

	public void setAuthLocation(String authLocation) {
		this.authLocation = authLocation;
	}

	public String getReviewTitle() {
		return reviewTitle;
	}

	public void setReviewTitle(String reviewTitle) {
		this.reviewTitle = reviewTitle;
	}

	public String getReviewId() {
		return reviewId;
	}

	public void setReviewId(String reviewId) {
		this.reviewId = reviewId;
	}

	public String getReviewComment() {
		return reviewComment;
	}

	public void setReviewComment(String reviewComment) {
		this.reviewComment = reviewComment;
	}

	public String getSelectedSentence() {
		return selectedSentence;
	}

	public void setSelectedSentence(String selectedSentence) {
		this.selectedSentence = selectedSentence;
	}

}
