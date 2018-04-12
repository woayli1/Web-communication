package pa;

public class User {
	long id ;
	String name;
	String pwd ;
	String truename;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getTruename() {
		return truename;
	}
	public void setTruename(String truename) {
		this.truename = truename;
	}
	public User(long id, String name, String pwd, String truename) {
		super();
		this.id = id;
		this.name = name;
		this.pwd = pwd;
		this.truename = truename;
	}
	public User() {
		super();
	}

}
