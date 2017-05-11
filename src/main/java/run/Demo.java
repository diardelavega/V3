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
import java.util.stream.Stream;

import objects.HotelObj;
import operations.CompareTopic;
import operations.ReadHotelFiles;
import utils.Commons;
import utils.GlobalVar;

public class Demo {

	public static void main(String[] args) throws IOException {
		 demoRun(args);
	}

	public static void loadConfigs(String reviewDirPath) throws NumberFormatException, IOException {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		BufferedReader bfr = new BufferedReader(new InputStreamReader(classloader.getResourceAsStream("config")));
		String line;
		String sentenceTokenizer = null;
		String posTagger = null;
		while ((line = bfr.readLine()) != null) {
			if (line.startsWith("#") || line.equals(""))
				continue;
			if (line.startsWith("topic_range"))
				GlobalVar.TOPIC_RANGE = Integer.parseInt(line.split("=")[1]);
			else if (line.startsWith("sentence_token"))
				sentenceTokenizer = line.split("=")[1].replaceAll("\"", "");
			else if (line.startsWith("pos_tager"))
				posTagger = line.split("=")[1].replaceAll("\"", "");
		} // while

		// init semantic structs and nlp model file paths
		Commons.init(reviewDirPath, sentenceTokenizer, posTagger);
	}

	public static void demoRun(String[] args) throws IOException {

		if (args.length > 0) {
			String folderPath = args[0];
			File dataFolder = new File(folderPath);
			if (!dataFolder.exists()) {
				throw new FileNotFoundException();
			} else if (!dataFolder.isDirectory()) {
				throw new FileNotFoundException("this is not a folder/directory");
			} else if (dataFolder.list().length == 0) {
				throw new FileNotFoundException("this folder is empty");
			}
			loadConfigs(args[0]);

			String topic = args[1];
			ReadHotelFiles rhf;

			List<HotelObj> hotelList = new ArrayList<>();
			for (String f : dataFolder.list()) {
				String fileName = folderPath + "/" + f;
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
				System.out.printf("Hotel: %s, points= %.3f \n", hotelList.get(i).getInfo().getName(),
						hotelList.get(i).getTsr().getTotalSentimentPoints());
				hotelList.get(i).getTsr().printExample();
			}
			System.out.println();
		}

	}
}
