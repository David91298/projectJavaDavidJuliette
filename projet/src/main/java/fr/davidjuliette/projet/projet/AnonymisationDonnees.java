package fr.davidjuliette.projet.projet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import regles.BeADauphineEmail;
import regles.BeAnAge;
import regles.BeAnEmail;
import regles.RandomLetter;
import regles.RandomLetterForLocalPart;
import regles.RegleVerification;
import regles.RegleAnonymisation;

public class AnonymisationDonnees extends TraitementDonnees {

	private String fileDescriptionAnonymized;

	public AnonymisationDonnees(String fileInput, String fileDescription, String fileDescriptionAnonymized,
			String fileOutput) {

		super(fileInput, fileDescription, fileOutput);
		this.fileDescriptionAnonymized = fileDescriptionAnonymized;
	}

	@Override
	public void traiterDonnees() {
		System.out.println("Début lecture fichier ...");
		//Lecture du fichier avec les données à vérifier
		List<String> inputArray = lireFichier(this.getFileInput());
		this.setEnTete(inputArray.get(0).split(";"));
		inputArray.remove(0); // Pour n'avoir que la data
		
		System.out.print("\tContenu entete: ");
		for (int i = 0; i < this.getEnTete().length; i++) {
			System.out.print(this.getEnTete()[i]+" | ");
		}
		
		//Lecture du fichier contenant la description du fichier avec les données		
		List <String> fileDescriptorArray = this.lireJSON(getFileDescription(),"dataType");
		
		System.out.println("\n\tDonnées lues dans le fichier input:" + inputArray);
		System.out.println("\n\tDonnées lues dans le champs 'dataType du fichier Datatype JSON:" + fileDescriptorArray);
		
		//Vérifie le type de chaque champ
		System.out.println("\nDébut vérification type...");
		List<String> dataTypeVerified = verifierType(inputArray,fileDescriptorArray);
		System.out.println("Vérification type terminée: " + dataTypeVerified);

		//Lecture du fichier décrivant les données à vérifier
		System.out.println("\nDébut lecture fichier decrivant les règles ...");
		List <String> fileRuleTilteArray = this.lireJSON(fileDescriptionAnonymized,"name");
		System.out.println("\n\tDonnées lues dans le champ 'name' du fichier JSON:" + fileRuleTilteArray);
		List <String> fileRuleDescriptorArray = this.lireJSON(fileDescriptionAnonymized,"changeTo");
		System.out.println("\n\tDonnées lues dans le champ 'should' du fichier JSON:" + fileRuleDescriptorArray);
		
		//Construction d'une liste intérmédiaire entre enTete, fileRuleTilteArray et fileRuleTilteArray
		String[] arrayIntemediate = buildArrayIntermediate(fileRuleTilteArray, fileRuleDescriptorArray);
		System.out.print("\n\tTableau intermédiaire: ");
		for (int i = 0; i < arrayIntemediate.length; i++) {
			System.out.print(arrayIntemediate[i]+" | ");
		}
		
		
		//Vérification des règles
		RandomLetter rd =  new RandomLetter("RANDOM_LETTER"); RandomLetterForLocalPart rdflp = new RandomLetterForLocalPart("RANDOM_LETTER_FOR_LOCAL_PART");
		List <RegleAnonymisation> listRules = new ArrayList<>();
		listRules.add(rd);listRules.add(rdflp);
		
		System.out.println("\nDébut application des règles ...");
		//L'Array list à transformer en fichier csv
		List <String> output = appliquerRegle(inputArray, listRules, arrayIntemediate);
		System.out.println(output);
		
		System.out.println("\nGénération du fichier output ...");
		writeFile(this.getFileOutput(), output);

		
	}
	
	/**
     * Vérifie les regles dans les champs correspondants et supprime les lignes qui ne respectent pas la regle
     * @param input
     * @param listRules
     * @param tabRules
     * @return
     */
    private List <String> appliquerRegle(List <String> input, List <RegleAnonymisation> listRules, String [] tabRules) {
    	//Pour chaque ligne de l'input, appliquer les regle demandé
		for (int i = 0; i < input.size(); i++) {
			System.out.println("\n\tDebut impression ligne " + (i+1));
			String ligne = input.get(i);
			String [] data = ligne.split(";");
			System.out.print("\tContenu data: ");
			for (int l = 0; l < data.length; l++) {
				System.out.print(data[l]+" | ");
			}
			//On vérifie les regle à appliquer
			for (int j = 0; j < tabRules.length; j++) {
				if (!tabRules[j].equals("")) {
					System.out.println("\n\t\tApplication de la règle: " + tabRules[j] + " à " + data[j]);
					//Pour chaque regle, verifier si il y a plusieurs regle à appliquer
					for (int k = 0; k < listRules.size(); k++) {
						tabRules[j] = tabRules[j].replaceAll("[\\W]", "");
						if (tabRules[j].replace('"', '.').equals(listRules.get(k).getName())) {
							//On applique la regle au data: différent pour anonymisation
							String dataAnonymized = listRules.get(k).appliquerRegle(data[j]);
							data[j] = dataAnonymized;
							String result="";
							for (int x = 0; x < data.length; x++) {
								result = result.concat(data[x]+";");
							}
							System.out.println("contenu data= " + result);
							input.set(i, result.substring(0,result.length()-1));
							System.out.println(input.get(i));
						}
					}
				}
			}
			
		}
    	return input;
	}    


}
