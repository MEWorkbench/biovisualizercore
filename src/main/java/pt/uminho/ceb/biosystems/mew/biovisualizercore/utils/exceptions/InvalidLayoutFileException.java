package pt.uminho.ceb.biosystems.mew.biovisualizercore.utils.exceptions;

public class InvalidLayoutFileException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Integer line = null;
	String reason;
	
	public InvalidLayoutFileException(Integer line, String reason){
		this.line = line;
		this.reason = reason;
	}
	
	public InvalidLayoutFileException(String reason){
		this(null, reason);
	}
	
	
	public String getMessage(){
		
		if(line == null) return reason;
		else return "Error in line: " + line + ". " + reason;
	}
}
