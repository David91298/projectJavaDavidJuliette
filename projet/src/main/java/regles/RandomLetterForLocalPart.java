package regles;

import java.util.Random;

public class RandomLetterForLocalPart extends Regle {

	public RandomLetterForLocalPart(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean appliquerRegle(String data) {
		
		Random r = new Random();
		
		String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

		//Example
		String mail = "david912@gmail.com";

		String[] t = mail.split("@");

		String localPart = t[0];

		StringBuilder newMail = new StringBuilder(localPart);

		for (int i = 0; i < newMail.length(); i++) {
			char randomCharacter = alphabet.charAt(r.nextInt(alphabet.length()));
			newMail.setCharAt(i, randomCharacter);
		}

		newMail.append("@" + t[1]);

		System.out.println(newMail);

		return false;
	}

}
