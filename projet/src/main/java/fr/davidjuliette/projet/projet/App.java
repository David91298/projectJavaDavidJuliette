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
				absolutePath + "/src/main/java/fr/davidjuliette/projet/projet/outputVerification.csv");
		vd.traiterDonnees();
    	
		
   	 	//Execution en mode Anonymisation données
		System.out.println("-------------------Anonymisation données-------------------");
		AnonymisationDonnees ad = new AnonymisationDonnees(absolutePath + "/src/main/java/fr/davidjuliette/projet/projet/input.csv", 
				absolutePath + "/src/main/java/fr/davidjuliette/projet/projet/csvDescriptor.json", 
				absolutePath + "/src/main/java/fr/davidjuliette/projet/projet/regleAnonyme.json", 
				absolutePath + "/src/main/java/fr/davidjuliette/projet/projet/outputAnonymisation.csv");
		ad.traiterDonnees();
        
        
       
    }
}
