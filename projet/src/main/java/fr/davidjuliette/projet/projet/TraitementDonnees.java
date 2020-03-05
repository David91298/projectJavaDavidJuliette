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

public abstract class TraitementDonnees {
	private FileReader fileReader;
	private BufferedReader br;
	private BufferedWriter bw;
	private FileWriter fileWriter;
	private String fileInput;
	private String fileDescription;
	private String fileOutput;
	private String [] enTete;

	
	public TraitementDonnees(String fileInput, String fileDescription, String fileOutput) {
		this.fileInput = fileInput;
		this.fileDescription = fileDescription;
		this.fileOutput = fileOutput;
	}
	
	public abstract void traiterDonnees();

	/**
	 * Permet de lire un fichier
	 * @param fileName
	 * @return
	 */
	protected List<String> lireFichier(String fileName) {
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
	protected List <String> lireJSON(String fileName, String field) {
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
    protected String[] buildArrayIntermediate(List <String> titleArray, List <String> rulesArray) {
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
	
	public String getFileInput() {
		return fileInput;
	}

	public String getFileDescription() {
		return fileDescription;
	}

	public String getFileOutput() {
		return fileOutput;
	}
	
	public String[] getEnTete() {
		return enTete;
	}
	
	public void setEnTete(String[] enTete) {
		this.enTete = enTete;
	}
	
	
}
