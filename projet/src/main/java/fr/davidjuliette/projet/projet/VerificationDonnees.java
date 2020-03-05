package fr.davidjuliette.projet.projet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import regles.BeADauphineEmail;
import regles.BeAnAge;
import regles.BeAnEmail;
import regles.Regle;
import regles.ReglesVerificationDonnees;

public class VerificationDonnees extends TraitementDonnees {
	
	private String fileRuleDescriptor;
	private FileReader fileReader;
	private BufferedReader br;
	private BufferedWriter bw;
	private FileWriter fileWriter;
	private String [] enTete;

	public VerificationDonnees(String fileInput, String fileDescription, String fileRuleDescriptor, String fileOutput) {
		super(fileInput, fileDescription, fileOutput);
		this.fileRuleDescriptor = fileRuleDescriptor;
	}
	
	@Override//
	public void traiterDonnees() {
		System.out.println("Début lecture fichier ...");
		//Lecture du fichier avec les données à vérifier
		List<String> inputArray = this.lireFichier(this.getFileInput());
		enTete = inputArray.get(0).split(";");
		inputArray.remove(0); // Pour n'avoir que la data
		
		System.out.print("\tContenu entete: ");
		for (int i = 0; i < enTete.length; i++) {
			System.out.print(enTete[i]+" | ");
		}
		
		//Lecture du fichier contenant la description du fichier avec les données		
		List <String> fileDescriptorArray = this.lireJSON(getFileDescription(),"dataType");
		
		System.out.println("\n\tDonnées lues dans le fichier input:" + inputArray);
		System.out.println("\n\tDonnées lues dans le champs 'dataType du fichier Datatype JSON:" + fileDescriptorArray);
		
		//Vérifie le type de chaque champ
		System.out.println("\nDébut vérification type...");
		List<String> dataTypeVerified = this.verifierType(inputArray,fileDescriptorArray);
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
		List <Regle> listRules = new ArrayList<>();
		listRules.add(beAnAge);listRules.add(beAnEmail);listRules.add(beDauphineEmail);
		
		System.out.println("\nDébut application des règles ...");
		//L'Array list à transformer en fichier csv
		System.out.println(appliquerRegle(inputArray, listRules, arrayIntemediate));
	}
	
	/**
	 * Vérifie si le type de chaque colonne est conforme, supprime les donnnées non conforme au type
	 * @param inputArray
	 * @param fileDescriptorArray
	 * @return
	 */
	private List<String> verifierType(List <String> inputArray, List <String> fileDescriptorArray) {		
		//Pour chaque ligne de l'input, vérifier le type
		for (int i = 0; i < inputArray.size(); i++) {
			System.out.println("\tDebut impression ligne " + (i+1));
			String ligne = inputArray.get(i);
			String [] elm = ligne.split(";");
			for (int j = 0; j < elm.length; j++) {
				boolean isSameType = comparer(elm[j], fileDescriptorArray.get(j));
				System.out.println("\t\tComparaison " + elm[j] + " et " + fileDescriptorArray.get(j) + " = " + isSameType );
				if (!isSameType) {
					//Si le type ne coincide pas: supprimer la ligne
					inputArray.remove(i);
					break;
				}
			}
		}
		return inputArray;
	}
	
	/**
	 * Compare les données avec le type attendu
	 * @param data
	 * @param type
	 * @return
	 */
	private boolean comparer(String data, String type) {
		boolean result = true;
		if(type.equals("STRING")) {
			if(isDouble(data))
				result = false;
		}
		else if(type.equals("INT")) {
			if(!isInt(data))
				result = false;
		}
		else if(type.equals("DOUBLE")) {
			if(!isDouble(data))
				result = false;
		}
		
		return result;
	}
	
	/**
	 * Vérifie si une valeur est un entier
	 * @param data
	 * @return
	 */
	private static boolean isInt(String data) {
	    if (data == null) {
	        return false;
	    }
	    try {
	        int d = Integer.parseInt(data);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	
	/**
	 * Vérifie si la valeur strNum est un boolean(marche aussi pour int)
	 * @param data
	 * @return
	 */
	private static boolean isDouble(String data) {
	    if (data == null) {
	        return false;
	    }
	    try {
	        double d = Double.parseDouble(data);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	
	/**
	 * Permet de lire un fichier
	 * @param fileName
	 * @return
	 */
	private List<String> lireFichier(String fileName) {
		List <String> contenuFichier = new ArrayList<String>();
		
		try {
			fileReader = new FileReader(fileName);
			br = new BufferedReader(fileReader);
			
			//Lecture ligne par ligne
			String row;
			try {
				while((row = br.readLine()) != null ) {
					String[] data = row.split(":");
					for (int i = 0; i < data.length; i++) {
						contenuFichier.add(data[i]);
					}
				} 
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e){
			e.printStackTrace();
		}	
		
		return contenuFichier;

	}
	
	/**
	 * Permet de lire un fichier JSON
	 * @param fileName
	 * @return
	 */
	private List <String> lireJSON(String fileName, String field) {
		List <String> contenuFichier = new ArrayList <String>();
        JSONParser jsonParser = new JSONParser();
        
        try (FileReader reader = new FileReader(fileName))
        {
            //Lecture du fichier JSON
            Object obj = jsonParser.parse(reader);
            JSONArray contentList = (JSONArray) obj;
             
            //Pour chaque objet du fichier, on specifie le champ qu'on veut
            for (Object object : contentList) {
            	contenuFichier.add(getDataTypeContent( (JSONObject) object, field));
			} 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
       
        return contenuFichier;

	}
        
	/**
	 * Simplifie la récupération des champs du JSON pour le fileDescriptor
	 * @param content
	 * @return
	 */
    private String getDataTypeContent(JSONObject content, String field) 
    {
    	if(content.get(field) instanceof JSONArray) {
    		return content.get(field).toString();
    	}
        return (String) content.get(field);
    }
	
    /**
     * Permet de construire un tableau qui a les règles dans les ieme colonne des titres
     * @param titleArray
     * @param rulesArray
     * @return
     */
    private String[] buildArrayIntermediate(List <String> titleArray, List <String> rulesArray) {
    	String [] result= new String [enTete.length];
    	for (int i = 0; i < enTete.length; i++) {
			if (titleArray.contains(enTete[i])) { 
				for (int j = 0; j < titleArray.size(); j++) 
					if(titleArray.get(j).equals(enTete[i])) 
						result[i] = rulesArray.get(j);
			}
			else
				result[i] = "";
		}
    	
    	return result;
    	
	}
    
    /**
     * Vérifie les regles dans les champs correspondants et supprime les lignes qui ne respectent pas la regle
     * @param input
     * @param listRules
     * @param tabRules
     * @return
     */
    private List <String> appliquerRegle(List <String> input, List <Regle> listRules, String [] tabRules) {
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
    
    
    //Exemple de traitement
	public static void main(String[] args) {
		String absolutePath = System.getProperty("user.dir");
		VerificationDonnees vd = new VerificationDonnees(absolutePath + "/src/main/java/fr/davidjuliette/projet/projet/input.csv", 
				absolutePath + "/src/main/java/fr/davidjuliette/projet/projet/csvDescriptor.json", 
				absolutePath + "/src/main/java/fr/davidjuliette/projet/projet/dataDescriptor.json", 
				absolutePath + "/src/main/java/fr/davidjuliette/projet/projet/output.csv");
		vd.traiterDonnees();
		
	}
	
}
