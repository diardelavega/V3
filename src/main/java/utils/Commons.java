package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.stemmer.PorterStemmer;

/**
 * @author Administrator this class will contain structures that load the
 *         semantic file informations
 */
public class Commons {
	public static Map<String, Float> positive = null;
	public static Map<String, Float> negative = null;
	public static Map<String, Float> intensifiers = null;

	private static SentenceDetectorME sdetector;
	private static POSTaggerME tagger;

	public static SentenceDetectorME getSentenceDetectorME() throws IOException {
		if (sdetector == null) {
			InputStream is = new FileInputStream("C:/Users/Administrator/Documents/opennlp/models/en-sent.bin");
			SentenceModel model = new SentenceModel(is);
			sdetector = new SentenceDetectorME(model);
		}
		return sdetector;
	}

	public static POSTaggerME getPOSTaggerME() throws IOException {
		if (tagger == null) {
			File file = new File("C:/Users/Administrator/Documents/opennlp/models/en-pos-maxent.bin");
			POSModel pmodel = new POSModelLoader().load(file);
			tagger = new POSTaggerME(pmodel);
		}
		return tagger;
	}

	/*
	 * read the data from the json file and and stem them to find them in the
	 * stemmed comment words. Phrases with multiple will not be stemmed as they
	 * are considered phrases/expressions that go togather as they are.
	 */
	public static void init(String filename) throws FileNotFoundException {
		// String filename = "C:/hotel/Semantic/semantics.json";
		filename += "/Semantic/semantics.json";
		System.out.println("@Commons filename " + filename);
		File f = new File(filename);
		if (!f.exists()) {
			throw new FileNotFoundException();
		}

		JsonParser parser = new JsonParser();
		JsonReader jr = new JsonReader(new FileReader(filename));
		JsonElement element = parser.parse(jr);
		JsonObject jobj = element.getAsJsonObject();
		PorterStemmer ps = new PorterStemmer();

		positive = new HashMap<String, Float>();
		JsonArray sentimentToPoint = jobj.getAsJsonArray("positive");
		String phrase;
		float val;
		JsonObject phraseValue;
		for (JsonElement je : sentimentToPoint) {
			phraseValue = je.getAsJsonObject();
			phrase = phraseValue.getAsJsonObject().get("phrase").getAsString();
			if (phrase.contains(" ")) {// two rods or more
				val = Float.parseFloat(phraseValue.getAsJsonObject().get("value").getAsString());
				positive.put(phrase, val);
			} else {
				String stemPhrase = ps.stem(ps.stem(phrase));
				val = Float.parseFloat(phraseValue.getAsJsonObject().get("value").getAsString());
				positive.put(stemPhrase, val);
			}
		}

		negative = new HashMap<String, Float>();
		sentimentToPoint = jobj.getAsJsonArray("negative");
		for (JsonElement je : sentimentToPoint) {
			phraseValue = je.getAsJsonObject();
			phrase = phraseValue.getAsJsonObject().get("phrase").getAsString();
			if (phrase.contains(" ")) {// two rods or more
				val = Float.parseFloat(phraseValue.getAsJsonObject().get("value").getAsString());
				negative.put(phrase, val);
			} else {
				val = Float.parseFloat(phraseValue.getAsJsonObject().get("value").getAsString());
				String stemPhrase = ps.stem(ps.stem(phrase));
				negative.put(stemPhrase, val);
			}
		}

		intensifiers = new HashMap<String, Float>();
		sentimentToPoint = jobj.getAsJsonArray("intensifier");
		for (JsonElement je : sentimentToPoint) {
			phraseValue = je.getAsJsonObject();
			phrase = phraseValue.getAsJsonObject().get("phrase").getAsString();
			if (phrase.contains(" ")) {// two rods or more
				val = Float.parseFloat(phraseValue.getAsJsonObject().get("multiplier").getAsString());
				intensifiers.put(phrase, val);
			} else {
				val = Float.parseFloat(phraseValue.getAsJsonObject().get("multiplier").getAsString());
				String stemPhrase = ps.stem(ps.stem(phrase));
				intensifiers.put(stemPhrase, val);
			}
		}
	}

}
