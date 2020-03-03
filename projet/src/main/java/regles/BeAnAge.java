package regles;

public class BeAnAge extends Regle {

	public BeAnAge(String name) {
		super(name);
	}

	@Override
	public boolean appliquerRegle(String data) {
		return (Integer.parseInt(data)>0 && Integer.parseInt(data)<150)? true : false;
	}

	public static void main(String[] args) {
		BeAnAge be = new BeAnAge("nom");
		System.out.println(be.appliquerRegle("211"));
	}
}
