package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels;

public class SeparatorsUtils {

	public static String[] names = new String[]{"NONE","SPACE","COMA", "SEMI-COMA", "TAB", "NEW LINE", "|", "#"};
	
	public static String convertNameToSep(String name){
		if(name.equals("SPACE"))
			return " ";
		if(name.equals("COMA"))
			return ",";
		if(name.equals("SEMI-COMA"))
			return ";";
		if(name.equals("TAB"))
			return "\t";
		if(name.equals("NEW LINE"))
			return "\n";
		if(name.equals("NONE"))
			return "";
		return name;
	}
	
	public static String convertSepToName(String sep){
		if(sep.equals(" "))
			return "SPACE";
		if(sep.equals(","))
			return "COMA";
		if(sep.equals(";"))
			return "SEMI-COMA";
		if(sep.equals("\t"))
			return "TAB";
		if(sep.equals("\n"))
			return "NEW LINE";
		if(sep.equals(""))
			return "NONE";
		return sep;
	}
}
