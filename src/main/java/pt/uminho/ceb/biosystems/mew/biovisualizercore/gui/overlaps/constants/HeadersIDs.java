package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.overlaps.constants;

import java.util.regex.Pattern;

public class HeadersIDs {
	
	public static final String	NOTHING			= "---";
	public static final String 	ID				= "ID";
	public static final String	VALUE			= "VALUE";
	public static final Pattern	VALUE_PATTERN	= Pattern.compile("^" + VALUE + "\\[(.*)\\]");
	
	public static final String	MET_ID			= ID;
	public static final String	MET_FORMULA		= "FORMULA";
	public static final String	MET_VALUE		= VALUE;
	
	public static final String	GENE_ID			= ID;
	public static final String	GENE_VALUE		= VALUE;
	
	public static final String	REACTION_ID		= ID;
	public static final String	REACTION_NAME	= "NAME";
	public static final String	REACTION_VALUE	= VALUE;
	
	
}


