package objects;

/**
 * @author Administrator
 *
 *         To keep count of the properties that the hotel is evaluated with
 *         points in the reviews, like: ["Cleanliness","Sleep Quality",
 *         "Overall", "Service", ...] . Get all of them from the data files and
 *         aggregate them to produce more information for a hotel. For every
 *         different attribute that an evaluation has been given, count how many
 *         times this attribute has been mentioned by a client (not every
 *         attribute is mentioned in every review) and add up the points that it
 *         has been awarded.
 * 
 * 
 */
public class CountNValue {

	private int propertyCount = 0;
	private float valueSum = 0;


	public void addValCount(float userVal) {
		propertyCount++;
		valueSum += userVal;

	}

	public int getPropertyCount() {
		return propertyCount;
	}

	public float getValueSum() {
		return valueSum;
	}

	public float properyAvgPoint() {
		return valueSum / propertyCount;
	}

}
