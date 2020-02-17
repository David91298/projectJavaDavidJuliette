package fr.davidjuliette.projet.projet;

public abstract class TraitementDonnees {
	private String fileInput;
	private String fileDescription;
	private String fileOutput;
	
	public TraitementDonnees(String fileInput, String fileDescription, String fileOutput) {
		this.fileInput = fileInput;
		this.fileDescription = fileDescription;
		this.fileOutput = fileOutput;
	}
	
	public abstract void traiterDonnees();
	
	public void verifierType() {
		// Some code
	}
}
