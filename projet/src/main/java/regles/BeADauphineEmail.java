package regles;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BeADauphineEmail extends Regle {

	public BeADauphineEmail(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean appliquerRegle(String data) {
		Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@dauphine+\\.eu");
        Matcher mat = pattern.matcher(data);
        
		return mat.matches()? true : false;
	}
	
	public static void main(String[] args){
        BeADauphineEmail be = new BeADauphineEmail("cdse");
        System.out.println(be.appliquerRegle("csdc@dauphine.eu"));
    }
}
