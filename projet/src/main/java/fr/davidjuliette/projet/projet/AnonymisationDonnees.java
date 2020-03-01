package fr.davidjuliette.projet.projet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
