package objects;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 * 
 *         The class for object which will contain all the information regarding
 *         the a hotel whose reviews are being scanned to search for a topic
 *         information. The HotelObj has a map of the user evaluated hotel
 *         attributes, results regarding the search topic with its estimation and general
 *         information about the hotel in hand.
 * 
 */
public class HotelObj {
	private HotelInfo info;

	// point awarded attributes <attrName, <count/sum/avg.>>
	private Map<String, CountNValue> attributes = new HashMap<String, CountNValue>();

	// topic sentiment point <topicName, <point/sentence/review_info>>
	private TopicSentimentResults tsr = new TopicSentimentResults();

	public float getAttributeAvgPoint(String attribute) {
		return attributes.get(attribute).properyAvgPoint();
	}

	/*
	 * add automatically the value of the attribute to it's allready existing
	 * count and sum values
	 */
	public void addAttributeUserValue(String attribute, float val) {
		attributes.get(attribute).addValCount(val);
	}

	public CountNValue getAttributeData(String attribute) {
		return attributes.get(attribute);
	}

	public Map<String, CountNValue> getAttribute() {
		return attributes;
	}

	public void addAttribute(String attribute) {
		CountNValue cnv = new CountNValue();
		attributes.put(attribute, cnv);
	}

	public void setInfo(HotelInfo info) {
		this.info = info;
	}

	public void setAttributes(Map<String, CountNValue> attributes) {
		this.attributes = attributes;
	}

	public TopicSentimentResults getTsr() {
		return tsr;
	}

	public void setTsr(TopicSentimentResults tsr) {
		this.tsr = tsr;
	}

	public HotelInfo getInfo() {
		return info;
	}

	
}
