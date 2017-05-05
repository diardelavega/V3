package operations;

import java.util.Comparator;

import objects.HotelObj;

public class CompareTopic implements Comparator<HotelObj> {

	public int compare(HotelObj o1, HotelObj o2) {

		// return Float.compare(o1.getTsr().getTotalSentimentPoints(),
		// o2.getTsr().getTotalSentimentPoints());
		if (o1.getTsr().getTotalSentimentPoints() < o2.getTsr().getTotalSentimentPoints())
			return 1;
		else if (o1.getTsr().getTotalSentimentPoints() > o2.getTsr().getTotalSentimentPoints())
			return -1;
		else
			return 0;

	}

}
