package regles;

public abstract class RegleVerification {
	private String name;
	
	public RegleVerification(String name) {
		this.name = name;
	}
	
	public abstract boolean appliquerRegle(String data);
	
	public String getName() {
		return name;
	}
}
