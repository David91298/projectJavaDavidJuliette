package regles;

public abstract class RegleAnonymisation {
	private String name;
	
	public RegleAnonymisation(String name) {
		this.name = name;
	}
	
	public abstract String appliquerRegle(String data);
}
