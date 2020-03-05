package fr.davidjuliette.projet.projet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
    
	
	/**
	 * Vérifie si le type de chaque colonne est conforme, supprime les donnnées non conforme au type
	 * @param inputArray
	 * @param fileDescriptorArray
	 * @return
	 */
	protected List<String> verifierType(List <String> inputArray, List <String> fileDescriptorArray) {		
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
	
	protected void writeFile(String fileName, List <String> output) {
		String[] t = {"nom", "age", "email"};

		// create output csv file
		try (PrintWriter writer = new PrintWriter(new File(fileName))) {

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < enTete.length; i++) {
				sb.append(enTete[i]);
				if (i == enTete.length-1) sb.append('\n');
				else sb.append(';');
			}
		
			for (String string : output) {
				sb.append(string);
				sb.append('\n');
			}
			
			writer.write(sb.toString());

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}

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
