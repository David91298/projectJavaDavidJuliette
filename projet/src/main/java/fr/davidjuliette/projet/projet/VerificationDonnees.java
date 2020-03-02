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
	
	private String fileDescriptionVerification;
	private FileReader fileReader;
	private BufferedReader br;
	private BufferedWriter bw;
	private FileWriter fileWriter;

	public VerificationDonnees(String fileInput, String fileDescription, String fileDescriptionVerification, String fileOutput) {
		super(fileInput, fileDescription, fileOutput);
		this.fileDescriptionVerification = fileDescriptionVerification;
	}
	
	@Override
	public void traiterDonnees() {
		//Lecture du fichier avec les données à vérifier
		List<String> inputArray = this.lireFichier(this.getFileInput());
		
		//Lecture du fichier contenant la description du fichier avec les données		
		List <String> fileDescriptorArray = this.lireJSON(getFileDescription());
		
		//Vérifie le type de chaque champ
		this.verifierType(inputArray,fileDescriptorArray);
		
		//Lecture du fichier décrivant les données à vérifier
		
		
	}
	
	/**
	 * Vérifie si le type de chaque colonne est conforme, supprime les donnnées non conforme au type
	 * @param inputArray
	 * @param fileDescriptorArray
	 */
	public void verifierType(List <String> inputArray, List <String> fileDescriptorArray) {
		// Some code
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
	private List <String> lireJSON(String fileName) {
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
            	contenuFichier.add(getDataTypeContent( (JSONObject) object ));
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
    private String getDataTypeContent(JSONObject content) 
    {
        return (String) content.get("dataType");
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
