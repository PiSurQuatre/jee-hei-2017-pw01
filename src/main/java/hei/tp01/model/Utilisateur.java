package hei.tp01.model;

public class Utilisateur {

	private String login;

	private String password;

	public Utilisateur() {
		super();
	}

	public Utilisateur(String login, String password) {
		super();
		this.login = login;
		this.password = password;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Utilisateur))
			return false;
		Utilisateur that = (Utilisateur) o;
		if (login != null ? !login.equals(that.login) : that.login != null)
			return false;
		return !(password != null ? !password.equals(that.password) : that.password != null);
	}


	@Override public int hashCode() {
		int result = login != null ? login.hashCode() : 0;
		result = 31 * result + (password != null ? password.hashCode() : 0);
		return result;
	}
}
