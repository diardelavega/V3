package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

/**
 * @author Administrator this class will contain structures that load the
 *         semantic file informations
 */
public class Commons {
	public static Map<String, Float> positive = null;
	public static Map<String, Float> negative = null;
	public static Map<String, Float> intensifiers = null;

	public static void init(String filename) throws FileNotFoundException {
//		String filename = "C:/hotel/Semantic/semantics.json";
		File f = new File(filename);
		if (!f.exists()) {
			throw new FileNotFoundException();
		}

		JsonParser parser = new JsonParser();
		JsonReader jr = new JsonReader(new FileReader(filename));
		JsonElement element = parser.parse(jr);
		JsonObject jobj = element.getAsJsonObject();

		positive = new HashMap<String, Float>();
		JsonArray pos = jobj.getAsJsonArray("positive");
		for (JsonElement je : pos) {
			JsonObject phraseValue = je.getAsJsonObject();
			String phrase = phraseValue.getAsJsonObject().get("phrase").getAsString();
			float val = Float.parseFloat(phraseValue.getAsJsonObject().get("value").getAsString());
			positive.put(phrase, val);
			// System.out.printf("%s : %.3f \n", phrase, val);
		}

		negative = new HashMap<String, Float>();
		JsonArray neg = jobj.getAsJsonArray("negative");
		for (JsonElement je : neg) {
			JsonObject phraseValue = je.getAsJsonObject();
			String phrase = phraseValue.getAsJsonObject().get("phrase").getAsString();
			float val = Float.parseFloat(phraseValue.getAsJsonObject().get("value").getAsString());
			negative.put(phrase, val);
			// System.out.printf("%s : %.3f \n", phrase, val);
		}

		intensifiers = new HashMap<String, Float>();
		JsonArray intens = jobj.getAsJsonArray("intensifier");
		for (JsonElement je : intens) {
			JsonObject phraseValue = je.getAsJsonObject();
			String phrase = phraseValue.getAsJsonObject().get("phrase").getAsString();
			float val = Float.parseFloat(phraseValue.getAsJsonObject().get("multiplier").getAsString());
			intensifiers.put(phrase, val);
			// System.out.printf("%s : %.3f \n", phrase, val);
		}

	}
}
