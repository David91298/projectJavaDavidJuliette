package regles;

import java.util.ArrayList;

public class ReglesVerificationDonnees {
	private ArrayList<Regle> listeRegles;
	
	public ReglesVerificationDonnees(ArrayList<Regle> listeRegles) {
		this.listeRegles = listeRegles;
	}
	
	public void ajouterRegle(Regle regle) {
		listeRegles.add(regle);
	}
	
	public void supprimerRegle(Regle regle) {
		listeRegles.remove(regle);
	}
}
