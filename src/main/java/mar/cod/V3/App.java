package mar.cod.V3;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.ml.model.PlainTextFileDataReader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.Span;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws InvalidFormatException, IOException {
		// System.out.println("Hello World!");
		// SentenceDetect();
		// chunk();
		// tokenize();
		// PorterStemmer ps= new PorterStemmer();
		// String s = ps.stem("unsetlling");
		// Stemmer st= new SnowballStemmer(ALGORITHM.ENGLISH);
		// System.out.println(st.stem("esential"));

		// String a =
		// "/ShowUserReviews-g32655-d76790-Reviews-BEST_WESTERN_PLUS_Eagle_Rock_Inn-Los_Angeles_California.html";
		// System.out.println(a.split("-Reviews-")[1].split(".html")[0]);

		// posTager();
		// chunk2();

	}

	public static void posTager() throws IOException {
		// String paragraph = "This place is oddly placed on the map or at least
		// to get to it but overall, it was decent. I wasn't ever worried about
		// loud neighbors or too much traffic even if it is off the highway. the
		// only place I ever heard the traffic was in the bathroom but the
		// window was open, so... it was quiet other than that. the little ponds
		// and decor had a japanese feel to it. it was very nice, great price,
		// and close to two freeways. Not much around the hotel but if you have
		// a car, you can get around and it's not that far from places (the
		// longest drive I communted was probably 30 minutes). Nice for couples
		// and families, not so great for frat parties. I'd definitely stay here
		// again.";

		String paragraph = "The product itself is good however this company has a terrible unrelying service.";
		File file = new File("C:/Users/Administrator/Documents/opennlp/models/en-pos-maxent.bin");
		POSModel pmodel = new POSModelLoader().load(file);
		POSTaggerME tagger = new POSTaggerME(pmodel);
		InputStream is = new FileInputStream("C:/Users/Administrator/Documents/opennlp/models/en-sent.bin");
		SentenceModel smodel = new SentenceModel(is);
		SentenceDetectorME sdetector = new SentenceDetectorME(smodel);

		String[] sentences = sdetector.sentDetect(paragraph);

		for (String sentence : sentences) {
			String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE.tokenize(sentence);
			String[] tags = tagger.tag(whitespaceTokenizerLine);
			for (int i = 0; i < whitespaceTokenizerLine.length; i++) {
				String word = whitespaceTokenizerLine[i].trim();
				String tag = tags[i].trim();
				System.out.println(whitespaceTokenizerLine[i] + "_" + word + ":" + tag + "_" + tags[i]);
			}
		}

	}

	public static void SentenceDetect() throws InvalidFormatException, IOException {
		String paragraph = "Pierre Vinken, 61 years old, will join the board as a nonexecutive director Nov. 29. mr. Vinken is chairman of Elsevier N.V., the Dutch publishing group. Rudolph Agnew, 55 years old and former chairman of Consolidated Gold Fields PLC, was named a director of this British industrial conglomerate.";

		// always start with a model, a model is learned from training data
		InputStream is = new FileInputStream("C:/Users/Administrator/Documents/opennlp/models/en-sent.bin");
		SentenceModel model = new SentenceModel(is);
		SentenceDetectorME sdetector = new SentenceDetectorME(model);
		for (String s : sdetector.sentDetect(paragraph)) {
			System.out.println(s);
		}
		// String sentences[] = sdetector.sentDetect(paragraph);
		//
		// System.out.println(sentences[0]);
		// System.out.println(sentences[1]);
		is.close();
	}

	public static void chunk2() throws InvalidFormatException, IOException {
		String sentence = "The product itself is good however this company has a terrible service.";
		WhitespaceTokenizer whitespaceTokenizer = WhitespaceTokenizer.INSTANCE;
		String[] tokens = whitespaceTokenizer.tokenize(sentence);
		for (String s : tokens)
			System.out.println(s);
		System.out.println();

		// Generating the POS tags
		File file = new File("C:/Users/Administrator/Documents/opennlp/models/en-pos-maxent.bin");
		POSModel model = new POSModelLoader().load(file);
		// Constructing the tagger
		POSTaggerME tagger = new POSTaggerME(model);
		// Generating tags from the tokens
		String[] tags = tagger.tag(tokens);
		for (String s : tags)
			System.out.println(s);
		System.out.println();

		// Loading the chunker model
		InputStream inputStream = new FileInputStream("C:/Users/Administrator/Documents/opennlp/models/en-chunker.bin");
		ChunkerModel chunkerModel = new ChunkerModel(inputStream);

		ChunkerME chunkerME = new ChunkerME(chunkerModel);

		// Generating the chunks
		String result[] = chunkerME.chunk(tokens, tags);

		for (int i = 0; i < result.length; i++)
			System.out.printf("%s : %s,  %s \n", tokens[i], tags[i], result[i]);
		// for (String s : result)
		// System.out.println(s);
	}

	public static void chunk() throws IOException {
		POSModel model = new POSModelLoader()
				.load(new File("C:/Users/Administrator/Documents/opennlp/models/en-pos-maxent.bin"));
		PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
		POSTaggerME tagger = new POSTaggerME(model);

		String input = "Hi. How are you? This is Mike.";
		ObjectStream<String> lineStream = (ObjectStream<String>) new PlainTextFileDataReader(
				new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

		perfMon.start();
		String line;
		String whitespaceTokenizerLine[] = null;

		String[] tags = null;
		while ((line = lineStream.read()) != null) {
			whitespaceTokenizerLine = WhitespaceTokenizer.INSTANCE.tokenize(line);
			tags = tagger.tag(whitespaceTokenizerLine);

			POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
			System.out.println(sample.toString());
			perfMon.incrementCounter();
		}
		perfMon.stopAndPrintFinalResult();

		// chunker
		InputStream is = new FileInputStream("en-chunker.bin");
		ChunkerModel cModel = new ChunkerModel(is);

		ChunkerME chunkerME = new ChunkerME(cModel);
		String result[] = chunkerME.chunk(whitespaceTokenizerLine, tags);

		for (String s : result)
			System.out.println(s);

		Span[] span = chunkerME.chunkAsSpans(whitespaceTokenizerLine, tags);
		for (Span s : span)
			System.out.println(s.toString());
	}

	public static void tokenize() throws InvalidFormatException, IOException {
		InputStream is = new FileInputStream("C:/Users/Administrator/Documents/opennlp/models/en-token.bin");

		// TokenizerModel model = new TokenizerModel(is);
		// Tokenizer tokenizer = new TokenizerME(model);

		// SimpleTokenizer st= SimpleTokenizer.INSTANCE;
		WhitespaceTokenizer st = WhitespaceTokenizer.INSTANCE;

		String tokens[] = st.tokenize(
				"Pierre Vinken, 61 years old, will join the board as a nonexecutive director Nov. 29. mr. Vinken is chairman of Elsevier N.V., the Dutch publishing group. Rudolph Agnew, 55 years old and former chairman of Consolidated Gold Fields PLC, was named a director of this British industrial conglomerate.");
		// Span tokenSpans[] = tokenizer.tokenizePos("An input sample
		// sentence.");

		for (String a : tokens)
			System.out.println(a);

		is.close();
	}

	public static void lemmatize() throws InvalidFormatException, IOException {
		InputStream is = new FileInputStream("C:/Users/Administrator/Documents/opennlp/models/en-token.bin");

		TokenizerModel model = new TokenizerModel(is);

		Tokenizer tokenizer = new TokenizerME(model);

		// String tokens[] = tokenizer.tokenize("Hi. How are you? This is
		// Mike.");
		Span tokenSpans[] = tokenizer.tokenizePos("An input sample sentence.");

		for (Span a : tokenSpans)
			System.out.println(a);

		is.close();
	}

}
