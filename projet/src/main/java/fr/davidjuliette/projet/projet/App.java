package fr.davidjuliette.projet.projet;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	
    	/*
    	 * Data initialization 
    	 */
    	
    	String fileInput = "C:/Users/david/OneDrive/Bureau/input.csv";
    	String fileDescription = "fileDescription.json";
    	String fileDescriptionAnonymized = "fileDescriptionAnonymized.json";
    	String fileOutput = "output.csv";
    	
        AnonymisationDonnees anonymisationDonnees = new AnonymisationDonnees(
        		fileInput, fileDescription, fileDescriptionAnonymized, fileOutput);
        
        anonymisationDonnees.traiterDonnees();
    }
}
