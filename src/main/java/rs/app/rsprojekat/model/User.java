package rs.app.rsprojekat.model;

import java.sql.Date;
import java.sql.Timestamp;
import javax.persistence.Entity;

@Entity
public class User {
    private int id_;
    private String ime_;
    private String prezime_;
    private String email_;
    private double wallet_;
    private boolean isAdmin_;
    private boolean isOrganizator_;
    private Date datumRod_;
    private Timestamp createdAt_;
    private boolean isLoggedIn_;

    public User() {
        id_ = 0;
        ime_ = prezime_ = email_ = "";
        wallet_ = 0.0;
        isAdmin_ = isOrganizator_ = false;
        datumRod_ =  Date.valueOf("2000-01-01");
        createdAt_ = Timestamp.valueOf("2000-01-01 00:00:00");
        isLoggedIn_ = false;
        return;
    }

    //Setters
    public User setId(int id) {
        id_ = id;
        return this;
    }

    public User setIme(String ime) {
        ime_ = ime;
        return this;
    }

    public User setPrezime(String prezime) {
        prezime_ = prezime;
        return this;
    }

    public User setEmail(String email) {
        email_ = email;
        return this;
    }

    public User setWallet(double value) {
        wallet_ = value;
        return this;
    }

    public User setIsAdmin(boolean isAdmin) {
        isAdmin_ = isAdmin;
        return this;
    }

    public User setIsOrganizator(boolean isOrganizator) {
        isOrganizator_ = isOrganizator;
        return this;
    }

    public User setDatumRod(Date datumRod) {
        datumRod_ = datumRod;
        return this;
    }

    public User setCreatedAt(Timestamp createdAt) {
        createdAt_ = createdAt;
        return this;
    }

    public User setIsLoggedIn(boolean isLoggedIn) {
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

    public Timestamp getCreatedAt() { return createdAt_; };

    public boolean isLoggedIn() { return isLoggedIn_; };
}