package fr.davidjuliette.projet.projet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class AnonymisationDonnees extends TraitementDonnees {

	private String fileDescriptionAnonymized;

	public AnonymisationDonnees(String fileInput, String fileDescription, String fileDescriptionAnonymized,
			String fileOutput) {

		super(fileInput, fileDescription, fileOutput);
		this.fileDescriptionAnonymized = fileDescriptionAnonymized;
	}

	@Override
	public void traiterDonnees() {
		// read data
		this.readData();
		
		String[] t = {"nom", "age", "email"};

		// create output csv file
		try (PrintWriter writer = new PrintWriter(new File("test.csv"))) {

			StringBuilder sb = new StringBuilder();
			sb.append("nom");
			sb.append(',');
			sb.append("age");
			sb.append(',');
			sb.append("email");
			sb.append('\n');
			
			for (int i = 0; i < 20 ; i++) {
				for(int j = 0 ; j < 3 ; j++) {
					sb.append(t[j]);
					
					if (j == 2) sb.append('\n');
					else sb.append(',');
				}
				
			}

			writer.write(sb.toString());

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	public void readData() {

		/********** Read csv file **********/
		File csvFile = new File(super.getFileInput());

		if (csvFile.isFile()) {
			// if csv file exists, then get all the columns

			try (BufferedReader csvReader = new BufferedReader(new FileReader(csvFile))) {

				String row = "";
				while ((row = csvReader.readLine()) != null) {
					// get all columns
					String[] data = row.split(",");

					for (String i : data) {
						System.out.println(i);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
