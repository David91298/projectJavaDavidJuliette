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
        Pattern pattern1 = Pattern.compile("[A-Za-z0-9._%+-]+@dauphine+\\.psl+\\.eu");
        Matcher mat1 = pattern.matcher(data);
        Pattern pattern2 = Pattern.compile("[A-Za-z0-9._%+-]+@lamsade+\\.dauphine+\\.fr");
        Matcher mat2 = pattern.matcher(data);
        
        
        
		return mat.matches() || mat1.matches() || mat2.matches()? true : false;
	}
	
}
