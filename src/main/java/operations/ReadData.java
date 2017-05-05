package operations;

import java.util.Map;

import objects.CountNValue;
import objects.HotelInfo;
import objects.TopicSentimentResults;

public interface ReadData {
	public HotelInfo getHotelInfo();

	public Map<String, CountNValue> getAttributes();

	public TopicSentimentResults getTopicResults();
}
