package regles;

import java.util.Random;

public class RandomLetter extends RegleAnonymisation {

	public RandomLetter(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String appliquerRegle(String data) {
		Random r = new Random();
    	
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
               
        StringBuilder newName = new StringBuilder(data);
        
        for (int i = 0 ; i < newName.length() ; i++) {
        	char randomCharacter = alphabet.charAt(r.nextInt(alphabet.length()));
        	newName.setCharAt(i, randomCharacter);
        }
        
        System.out.println("Donnée anonymisé = "+newName);
        
        return newName.toString();
	}

}
