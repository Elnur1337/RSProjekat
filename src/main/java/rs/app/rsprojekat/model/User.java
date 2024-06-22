package rs.app.rsprojekat.model;

import java.sql.Date;
import java.sql.Timestamp;
import javax.persistence.*;

@Entity
@Table(name = "korisnik")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id_;

    @Column(name = "ime", nullable = false, length = 20)
    private String ime_;

    @Column(name = "prezime", nullable = false, length = 20)
    private String prezime_;

    @Column(name = "email", nullable = false, length = 50, unique = true)
    private String email_;

    @Column(name = "pass", nullable = false, length = 250)
    private String pass_;

    @Column(name = "wallet", nullable = false)
    private double wallet_;

    @Column(name = "admin", nullable = false)
    private boolean isAdmin_;

    @Column(name = "organizator", nullable = false)
    private boolean isOrganizator_;

    @Column(name = "datum_rod")
    private Date datumRod_;

    @Column(name = "approved", nullable = false)
    private boolean approved_;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt_;

    private boolean isLoggedIn_;

    public User() {
        id_ = 0;
        ime_ = prezime_ = email_ = pass_ = "";
        wallet_ = 0.0;
        isAdmin_ = isOrganizator_ = approved_ = false;
        datumRod_ =  Date.valueOf("2000-01-01");
        createdAt_ = Timestamp.valueOf("2000-01-01 00:00:00");
        isLoggedIn_ = false;
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

    public User setPass(String pass) {
        pass_ = pass;
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

    public User setIsApproved(boolean isApproved) {
        approved_ = isApproved;
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

    public boolean isApproved() { return approved_; };

    public Timestamp getCreatedAt() { return createdAt_; };

    public boolean isLoggedIn() { return isLoggedIn_; };
}