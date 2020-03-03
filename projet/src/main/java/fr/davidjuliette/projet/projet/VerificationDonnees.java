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

public class VerificationDonnees extends TraitementDonnees {
	
	private String fileRuleDescriptor;
	private FileReader fileReader;
	private BufferedReader br;
	private BufferedWriter bw;
	private FileWriter fileWriter;

	public VerificationDonnees(String fileInput, String fileDescription, String fileRuleDescriptor, String fileOutput) {
		super(fileInput, fileDescription, fileOutput);
		this.fileRuleDescriptor = fileRuleDescriptor;
	}
	
	@Override
	public void traiterDonnees() {
		System.out.println("Début lecture fichier ...");
		//Lecture du fichier avec les données à vérifier
		List<String> inputArray = this.lireFichier(this.getFileInput());
		
		//Lecture du fichier contenant la description du fichier avec les données		
		List <String> fileDescriptorArray = this.lireJSON(getFileDescription(),"dataType");
		
		System.out.println("Données lues dans le fichier input:" + inputArray);
		System.out.println("Données lues dans le fichier Datatype JSON:" + fileDescriptorArray);
		
		//Vérifie le type de chaque champ
		System.out.println("Début vérification type...");
		List<String> dataTypeVerified = this.verifierType(inputArray,fileDescriptorArray);
		System.out.println("Vérification type terminée: " + dataTypeVerified);

		//Lecture du fichier décrivant les données à vérifier
		System.out.println("Début lecture fichier decrivant les règles ...");
		//List <String> fileRuleDescriptorArray = this.lireJSON(fileRuleDescriptor,"should");
		//System.out.println("Données lues dans le fichier JSON:" + fileRuleDescriptorArray);
		
		
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
			System.out.println("Debut impression ligne " + (i+1));
			String ligne = inputArray.get(i);
			String [] elm = ligne.split(";");
			for (int j = 0; j < elm.length; j++) {
				boolean isSameType = comparer(elm[j], fileDescriptorArray.get(j));
				System.out.println("Comparaison " + elm[j] + " et " + fileDescriptorArray.get(j) + " = " + isSameType );
				if (!isSameType) {
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
		///JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
         
        try (FileReader reader = new FileReader(fileName))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray contentList = (JSONArray) obj;
             
            //Iterate over employee array
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
        return (String) content.get(field);
    }
	
    
    //Pout tester A SUPPRIMER QD TEST FINI
	public static void main(String[] args) {
		String absolutePath = System.getProperty("user.dir");
		VerificationDonnees vd = new VerificationDonnees(absolutePath + "/src/main/java/fr/davidjuliette/projet/projet/input.csv", 
				absolutePath + "/src/main/java/fr/davidjuliette/projet/projet/csvDescriptor.json", 
				absolutePath + "/src/main/java/fr/davidjuliette/projet/projet/dataDescriptor.json", 
				absolutePath + "/src/main/java/fr/davidjuliette/projet/projet/output.csv");
		vd.traiterDonnees();
		
	}
	
}
