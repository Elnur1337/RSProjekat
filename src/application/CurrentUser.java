package application;

import java.sql.Date;

public class CurrentUser {
	private int id_;
	private String ime_;
	private String prezime_;
	private String email_;
	private double wallet_;
	private boolean isAdmin_;
	private boolean isOrganizator_;
	private Date datumRod_;
	private Date createdAt_;
	private boolean isLoggedIn_;
	
	public CurrentUser() {
		id_ = 0;
		ime_ = prezime_ = email_ = "";
		wallet_ = 0.0;
		isAdmin_ = isOrganizator_ = false;
		datumRod_ = createdAt_ = Date.valueOf("2000-01-01");
		isLoggedIn_ = false;
		return;
	}
	
	//Setters
	public CurrentUser setId(int id) {
		id_ = id;
		return this;
	}
	
	public CurrentUser setIme(String ime) {
		ime_ = ime;
		return this;
	}
	
	public CurrentUser setPrezime(String prezime) {
		prezime_ = prezime;
		return this;
	}
	
	public CurrentUser setEmail(String email) {
		email_ = email;
		return this;
	}
	
	public CurrentUser setWallet(double value) {
		wallet_ = value;
		return this;
	}
	
	public CurrentUser setIsAdmin(boolean isAdmin) {
		isAdmin_ = isAdmin;
		return this;
	}
	
	public CurrentUser setIsOrganizator(boolean isOrganizator) {
		isOrganizator_ = isOrganizator;
		return this;
	}
	
	public CurrentUser setDatumRod(Date datumRod) {
		datumRod_ = datumRod;
		return this;
	}
	
	public CurrentUser setCreatedAt(Date createdAt) {
		createdAt_ = createdAt;
		return this;
	}
	
	public CurrentUser setIsLoggedIn(boolean isLoggedIn) {
		isLoggedIn_ = isLoggedIn;
		return this;
	}
	
	//Getters
	public int getId() { return id_; };
	
	public String getIme() { return ime_; };
	
	public String getPrezime() { return prezime_; };
	
	public String getEmail() { return email_; };
	
	public double getWallet() { return wallet_; };
	
	public boolean isAdmin() { return isAdmin_; };
	
	public boolean isOrganizator() { return isOrganizator_; };
	
	public Date getDatumRod() { return datumRod_; };
	
	public Date getCreatedAt() { return createdAt_; };
	
	public boolean isLoggedIn() { return isLoggedIn_; };
}
