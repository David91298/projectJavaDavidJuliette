package regles;

public class BeAnAge extends RegleVerification {

	public BeAnAge(String name) {
		super(name);
	}

	@Override
	public boolean appliquerRegle(String data) {
		return (Integer.parseInt(data)>0 && Integer.parseInt(data)<150)? true : false;
	}

}
