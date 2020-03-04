package regles;

public abstract class Regle {
	private String name;
	
	public Regle(String name) {
		this.name = name;
	}
	
	public abstract boolean appliquerRegle(String data);
	
	public String getName() {
		return name;
	}
}
