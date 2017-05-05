package operations;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import objects.CountNValue;
import objects.HotelInfo;
import objects.ReviewInfo;
import objects.TopicSentimentResults;

/**
 * @author Administrator
 * 
 *         This Class is utilized to read the data from the file and search for
 *         the topic requested by the user. All the file has to be read to
 *         search in the comments for the topic.
 * 
 */
public class ReadHotelFiles implements ReadData {

	private Gson gson = new GsonBuilder().setPrettyPrinting().create();

	private HotelInfo hi = new HotelInfo();
	private Map<String, CountNValue> attCNV = new HashMap<String, CountNValue>();
	private TopicSentimentResults tsr = new TopicSentimentResults();

	public int readHotelData(String fileName, String topic) throws IOException {
		tsr.setTopic(topic);
		JsonReader jr;
		JsonParser parser = new JsonParser();

		// parse json file to json object to be able to travers it
		File file = new File(fileName);
		if (!file.isFile())
			return -1;
		jr = new JsonReader(new FileReader(file));
		JsonObject jobj = parser.parse(jr).getAsJsonObject();

		// request the hotel info sub-element for the HotelInfo obj.
		JsonObject hotelInfo = jobj.getAsJsonObject("HotelInfo");
		extractHotelInfo(hotelInfo);

		// go through the reviews to evaluate the hotel for a topic.
		JsonArray reviews = jobj.getAsJsonArray("Reviews");
		topicSearch(reviews, topic);
		return 0;
	}

	/*
	 * iterate through the reviews to find sentiment about a topic and get the
	 * ratings for hotel attributes
	 */
	private void topicSearch(JsonArray reviews, String topic) {
		TopicSearchEvaluation tse;

		for (JsonElement rev : reviews) {
			tse = new TopicSearchEvaluation();
			JsonObject revobj = rev.getAsJsonObject();

			// get the ratings for the evaluated hotel attributes
			JsonObject rating = revobj.getAsJsonObject("Ratings");
			extractAttributeRatings(rating);// add to attCNV map

			// estimate the topic sentiment from the comments
			String comment = revobj.get("Content").getAsString();
			tse.perSentenceSearch(comment, topic);
			if (tse.isFoundFlag()) {
				tsr.countTopicMentions();
				tsr.addToPositiveSentimentScore(tse.getCommentPositiveSentimentPoints());
				tsr.addToNegativeSentimentScore(tse.getCommentNegativeSentimentPoints());
				ReviewInfo rvi = extractReviewInfo(revobj);
				rvi.setSelectedSentence(tse.getSelectedSentence());
				tsr.addRepresentativeReview(rvi);
			}
		}
	}

	/*
	 * go through every hotel attributes rated by the user, to produce a better
	 * idea about the hotel general/common services
	 */
	private void extractAttributeRatings(JsonObject rating) {
		for (Map.Entry<String, JsonElement> entry : rating.entrySet()) {
			if (attCNV.containsKey(entry.getKey()))
				attCNV.get(entry.getKey()).addValCount(Float.parseFloat(entry.getValue().getAsString()));
			else {
				CountNValue cnv = new CountNValue();
				cnv.addValCount(Float.parseFloat(entry.getValue().getAsString()));
				attCNV.put(entry.getKey().toString(), cnv);
			} // else
		} // for

	}

	private ReviewInfo extractReviewInfo(JsonObject hotelInfo) {
		ReviewInfo rvi = new ReviewInfo();
		for (Entry<String, JsonElement> es : hotelInfo.entrySet()) {
			switch (es.getKey()) {
			case "Title":
				rvi.setReviewTitle(gson.fromJson(es.getValue(), String.class));
				break;
			case "Author":
				rvi.setAuthName(gson.fromJson(es.getValue(), String.class));
				break;
			case "ReviewID":
				rvi.setReviewId(gson.fromJson(es.getValue(), String.class));
				break;
			case "AuthorLocation":
				rvi.setAuthLocation(gson.fromJson(es.getValue(), String.class));
				break;
			case "Date":
				rvi.setDate(gson.fromJson(es.getValue(), String.class));
				break;
			}
		} // for

		return rvi;
	}

	private void extractHotelInfo(JsonObject revobj) {
		for (Entry<String, JsonElement> es : revobj.entrySet()) {
			switch (es.getKey()) {
			case "Name":
				hi.setName(gson.fromJson(es.getValue(), String.class));
				break;
			case "HotelURL":
				hi.setUrl(gson.fromJson(es.getValue(), String.class));
				break;
			case "Address":
				hi.setHtmlAddress(gson.fromJson(es.getValue(), String.class));
				break;
			case "HotelID":
				hi.setId(gson.fromJson(es.getValue(), String.class));
				break;
			case "ImgURL":
				hi.setImgUrl(gson.fromJson(es.getValue(), String.class));
				break;
			case "Price":
				hi.setPrice(gson.fromJson(es.getValue(), String.class));
				break;
			}
		}
	}

	private void resultPrint() {
		/*
		 * // TEST print results for (String key : attCNV.keySet()) {
		 * System.out.printf("%s :  %.3f   \n", key,
		 * attCNV.get(key).properyAvgPoint()); }
		 */
	}

	@Override
	public HotelInfo getHotelInfo() {
		// TODO Auto-generated method stub
		return hi;
	}

	@Override
	public Map<String, CountNValue> getAttributes() {
		return attCNV;
	}

	@Override
	public TopicSentimentResults getTopicResults() {
		return tsr;
	}

}
