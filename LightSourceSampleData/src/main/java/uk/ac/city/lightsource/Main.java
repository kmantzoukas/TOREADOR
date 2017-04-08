package uk.ac.city.lightsource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import uk.ac.city.lightsource.model.Appliance;
import uk.ac.city.lightsource.model.Measurement;
import uk.ac.city.lightsource.model.TYPE;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Main {

	private static final Random random = new Random(new Date().getTime());
	private static List<Measurement> measurements = new ArrayList<Measurement>();
	private static DecimalFormat df = new DecimalFormat("#.##");

	public static void main(String[] args) throws InterruptedException {

		System.out.println("\nProducing lightsource sample data...");
		
		for (int i = 0; i < Integer.valueOf(args[1]); i++) {
			
			measurements.add(createMeasurement(random));
			
			if((i+1) % 1000 == 0){
				System.out.println((i+1) + " measurements have been generated");
				writeSampleDataInFile(getFilename(args));
				measurements.clear();
			}
		}
	}

	private static void writeSampleDataInFile(String filename) {
		
		Gson gson = new GsonBuilder().create();

		Writer writer = null;

		try {

			writer = new FileWriter(filename,true);
			System.out.println("\nWritting sample data in file "
					+ new File(filename).getAbsolutePath());
			
			for(Measurement measurement : measurements){
				gson.toJson(measurement, writer);
				writer.append("\n");
			}
			
			System.out.println("\nFinished successfully.");

		} catch (IOException e) {
			System.out.println("Unable to produce sample data with error "
					+ e.getMessage());
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					System.out
							.println("Unable to close the file descriptor for file "
									+ filename);
				}
			}
		}
	}

	private static String getFilename(String[] args) {

		String filename = null;

		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
		String suffix = "light_source_sample_data_"
				+ dateFormat.format(cal.getTime()) + ".json";

		if (args.length == 0) {
			filename = suffix;
		} else {
			if (new File(args[0]).isDirectory()) {
				filename = suffix;
			} else {
				if (!args[0].endsWith(".json"))
					filename = args[0] + ".json";
				else
					filename = args[0];
			}
		}
		return filename;
	}

	private static Measurement createMeasurement(Random random) throws InterruptedException {

		Measurement m = new Measurement();
		m.setId(getRandomInteger(1, 10, random));
		m.setEnergyProduced(getRandomDouble(5000.0, 12000.0, random));
		Thread.sleep(getRandomInteger(1, 5, random));
		m.setTimestamp(new Date().getTime());

		List<Appliance> appliances = new ArrayList<Appliance>();

		/*
		 * Create refrigerator
		 */
		Appliance refrigerator = new Appliance();
		refrigerator.setApplianceType(TYPE.REFRIGERATOR);
		refrigerator.setConsumption(getRandomDouble(100.0, 250.0, random));
		appliances.add(refrigerator);

		/*
		 * Create stove
		 */
		Appliance stove = new Appliance();
		stove.setApplianceType(TYPE.STOVE);
		stove.setConsumption(getRandomDouble(300.0, 600.0, random));
		appliances.add(stove);

		/*
		 * Create kettle
		 */
		Appliance kettle = new Appliance();
		kettle.setApplianceType(TYPE.KETTLE);
		kettle.setConsumption(getRandomDouble(50.0, 80., random));
		appliances.add(kettle);

		/*
		 * Create television
		 */
		Appliance television = new Appliance();
		television.setApplianceType(TYPE.TELEVISION);
		television.setConsumption(getRandomDouble(300.0, 600., random));
		appliances.add(television);
		
		/*
		 * Create heater
		 */
		Appliance heater = new Appliance();
		heater.setApplianceType(TYPE.HEATER);
		heater.setConsumption(getRandomDouble(500.0, 600., random));
		appliances.add(heater);

		m.setAppliances(appliances);
		
		return m;
	}

	private static int getRandomInteger(int min, int max, Random random) {
		return random.nextInt((max - min) + 1) + min;
	}

	private static double getRandomDouble(double min, double max, Random random) {
		double d = min + (max - min) * random.nextDouble();
		return Double.parseDouble(df.format(d));
	}

}
