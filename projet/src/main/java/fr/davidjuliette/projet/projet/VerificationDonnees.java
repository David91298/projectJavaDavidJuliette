package fr.davidjuliette.projet.projet;


import java.util.ArrayList;
import java.util.List;
import regles.BeADauphineEmail;
import regles.BeAnAge;
import regles.BeAnEmail;
import regles.RegleVerification;

public class VerificationDonnees extends TraitementDonnees {
	
	private String fileRuleDescriptor;

	public VerificationDonnees(String fileInput, String fileDescription, String fileRuleDescriptor, String fileOutput) {
		super(fileInput, fileDescription, fileOutput);
		this.fileRuleDescriptor = fileRuleDescriptor;
	}
	
	@Override//
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
		List <String> fileRuleTilteArray = this.lireJSON(fileRuleDescriptor,"name");
		System.out.println("\n\tDonnées lues dans le champ 'name' du fichier JSON:" + fileRuleTilteArray);
		List <String> fileRuleDescriptorArray = this.lireJSON(fileRuleDescriptor,"should");
		System.out.println("\n\tDonnées lues dans le champ 'should' du fichier JSON:" + fileRuleDescriptorArray);
		
		//Construction d'une liste intérmédiaire entre enTete, fileRuleTilteArray et fileRuleTilteArray
		String[] arrayIntemediate = buildArrayIntermediate(fileRuleTilteArray, fileRuleDescriptorArray);
		System.out.print("\n\tTableau intermédiaire: ");
		for (int i = 0; i < arrayIntemediate.length; i++) {
			System.out.print(arrayIntemediate[i]+" | ");
		}
		
		//Vérification des règles
		BeAnAge beAnAge = new BeAnAge("BE_AN_AGE"); BeAnEmail beAnEmail = new BeAnEmail("BE_AN_EMAIL"); BeADauphineEmail beDauphineEmail = new BeADauphineEmail("BE_AN_DAUPHINE_EMAIL");
		List <RegleVerification> listRules = new ArrayList<>();
		listRules.add(beAnAge);listRules.add(beAnEmail);listRules.add(beDauphineEmail);
		
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
    private List <String> appliquerRegle(List <String> input, List <RegleVerification> listRules, String [] tabRules) {
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
					System.out.println("\n\t\tApplication de la règle: " + tabRules[j]);
					//Pour chaque regle, verifier si il y a plusieurs regle à appliquer
					for (int k = 0; k < listRules.size(); k++) {
						//Si il faut appliquer plusieurs regles: NE CONCERNE PAS ANONYMISATION
						if(tabRules[j].contains(",")) {
							System.out.println("\t\t" + tabRules[j] + "contient plusieur regle");
							String [] plusieurRegle = tabRules[j].split(",");
							//Application des regles une par une
							for (int m = 0; m < plusieurRegle.length; m++) {
								plusieurRegle[m] = plusieurRegle[m].replaceAll("[\\W]", "");
								System.out.println("\t\tRègle n°" + (m+1) + " " + plusieurRegle[m]);
								if (plusieurRegle[m].replace('"', '.').equals(listRules.get(k).getName())) {
									System.out.println("\t\t" + plusieurRegle[m] + " = à " + listRules.get(k).getName() + " donc application de la règle");
									//On applique la regle au data: différent pour anonymisation
									if(!listRules.get(k).appliquerRegle(data[j])) {
										System.out.println("\t\t" + data[j] + " ne respecte pas la regle " + listRules.get(k).getName());
										System.out.println("\n\t\t Suppression de : " + input.get(i));
										input.remove(i);
									}
								}
							}

						}
						//sinon
						else {
							tabRules[j] = tabRules[j].replaceAll("[\\W]", "");
							if (tabRules[j].replace('"', '.').equals(listRules.get(k).getName())) {
								System.out.println("\t\t" + tabRules[j] + " = à " + listRules.get(k).getName() + " donc application de la règle");
								//On applique la regle au data: différent pour anonymisation
								if(!listRules.get(k).appliquerRegle(data[j])) {
									System.out.println("\t\t" + data[j] + " ne respecte pas la regle " + listRules.get(k).getName());
									System.out.println("\n\t\t Suppression de : " + input.get(i));
									input.remove(i);
								}
							}
						}
					}
				}
			}
			
		}
    	return input;
	}    
	
}
