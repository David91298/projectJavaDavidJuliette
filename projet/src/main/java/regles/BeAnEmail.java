package regles;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BeAnEmail extends Regle {

	public BeAnEmail(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean appliquerRegle(String data) {
		Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mat = pattern.matcher(data);
        
		return mat.matches()? true : false;
	}
	
	public static void main(String[] args){
        BeAnEmail be = new BeAnEmail("cdse");
        System.out.println(be.appliquerRegle("csdc@ds;do"));
    }

}
