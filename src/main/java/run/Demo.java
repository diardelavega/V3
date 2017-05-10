package run;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import objects.HotelObj;
import operations.CompareTopic;
import operations.ReadHotelFiles;
import utils.Commons;

public class Demo {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		demoRun(args);
	}

	public static void demoRun(String[] args) throws IOException {
		
		if (args == null ||args.length<2) {
			args = new String[2];
//			args[0] = "C:/Users/diego/Desktop/challange";
			args[0] = "C:/hotel";
			args[1] = "breakfast";
		}
		System.out.println("@Demo args "+args[0]+" "+args[1]);
		Commons.init(args[0]);// init semantic data structs
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
