package pack;

public class ConfigException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7457522867423954208L;
	
	public ConfigException() {}
	public ConfigException(String message) { super(message);}
	public ConfigException(Throwable cause) { super(cause);}
	public ConfigException(String message, Throwable cause) { super(message, cause);}
}
