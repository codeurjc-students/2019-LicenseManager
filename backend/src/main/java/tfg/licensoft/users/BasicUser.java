package tfg.licensoft.users;
//This class is just for communication between external programs and our users
public class BasicUser{
	private String userName;
	private String password;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public BasicUser() {
	}
		
}
