package run;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import objects.CliResponse;
import objects.HotelObj;
import operations.CompareTopic;
import operations.ReadHotelFiles;
import utils.Commons;
import utils.GlobalVar;

public class Demo {

	public static void main(String[] args) throws Exception {
		
		CliResponse clirsp = new CLI(args).parse();
		if (clirsp.getDir() != null && clirsp.getTopic() != null)
			demoRun(clirsp);
	}

	public static void readAndInit(String reviewDirPath) throws IOException {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		Properties prop = new Properties();
		prop.load(new InputStreamReader(classloader.getResourceAsStream("config")));
		String sentenceTokenizer = null;
		String posTagger = null;

		if (prop.containsKey("sentence_token")) {
			sentenceTokenizer = prop.getProperty("sentence_token", "123");
		}
		if (prop.containsKey("pos_tager")) {
			posTagger = prop.getProperty("pos_tager", "123");
		}

		// init semantic structs and nlp model file paths
		Commons.init(reviewDirPath, sentenceTokenizer, posTagger);

	}


	public static void demoRun(CliResponse clirsp) throws IOException {

		File dataFolder = new File(clirsp.getDir());

		readAndInit(clirsp.getDir());
		String topic = clirsp.getTopic();
		ReadHotelFiles rhf;

		List<HotelObj> hotelList = new ArrayList<>();
		for (String f : dataFolder.list()) {
			String fileName = clirsp.getDir() + "/" + f;
			HotelObj hotel = new HotelObj();
			rhf = new ReadHotelFiles();
			if (rhf.readHotelData(fileName, topic) != -1) {
				hotel.setInfo(rhf.getHotelInfo());
				hotel.setAttributes(rhf.getAttributes());
				hotel.setTsr(rhf.getTopicResults());
				hotelList.add(hotel);
			}
		}

		Collections.sort(hotelList, new CompareTopic());
		for (int i = 0; i < hotelList.size(); i++) {
			System.out.printf("Hotel: %s, points= %.3f \n", hotelList.get(i).getInfo().getName(), hotelList.get(i).getTsr().getTotalSentimentPoints());
			if(clirsp.isHotlInfo()){
				System.out.println("hotel info----------------");
				hotelList.get(i).getInfo().printHotelInfo();
				System.out.println();
			}
			if(clirsp.isRatings()){
				System.out.println("attribute ratings----------------");
				for(String k:hotelList.get(i).getAttribute().keySet()){
					System.out.printf("%s : %.2f \n",k,hotelList.get(i).getAttribute().get(k).properyAvgPoint());
				}
			}
			
//			if(clirsp.isReviewDetails())
				hotelList.get(i).getTsr().printExample(clirsp.isReviewDetails(), clirsp.isRepresentativeSentences());
			
			
			System.out.println();
		}
		System.out.println();
	}

}
