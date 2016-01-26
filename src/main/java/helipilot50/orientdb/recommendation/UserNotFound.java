package helipilot50.orientdb.recommendation;

public class UserNotFound extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2689850285266341615L;
	private String provileName;

	public UserNotFound() {
		super();
	}


	public UserNotFound(String user, Throwable cause) {
		super("User not found: " + user, cause);
		this.provileName = user;
	}


	public UserNotFound(String user) {
		super("User not found: " + user);
		this.provileName = user;
	}


	public String getCustomerID() {
		return provileName;
	}

	
}
