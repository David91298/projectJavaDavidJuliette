package fr.davidjuliette.projet.projet;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	
    	 //Execution en mode Verification données
    	System.out.println("-------------------Verification données-------------------");
        String absolutePath = System.getProperty("user.dir");
		VerificationDonnees vd = new VerificationDonnees(absolutePath + "/src/main/java/fr/davidjuliette/projet/projet/input.csv", 
				absolutePath + "/src/main/java/fr/davidjuliette/projet/projet/csvDescriptor.json", 
				absolutePath + "/src/main/java/fr/davidjuliette/projet/projet/dataDescriptor.json", 
				absolutePath + "/src/main/java/fr/davidjuliette/projet/projet/output.csv");
		vd.traiterDonnees();
    	
    	/*
    	 * Data initialization 
    	 */
    	System.out.println("\n-------------------Anonymisations données-------------------");
    	String fileInput = "C:/Users/david/OneDrive/Bureau/input.csv";
    	String fileDescription = "fileDescription.json";
    	String fileDescriptionAnonymized = "fileDescriptionAnonymized.json";
    	String fileOutput = "output.csv";
    	
        AnonymisationDonnees anonymisationDonnees = new AnonymisationDonnees(
        		fileInput, fileDescription, fileDescriptionAnonymized, fileOutput);
        
        anonymisationDonnees.traiterDonnees();
        
        
       
    }
}
