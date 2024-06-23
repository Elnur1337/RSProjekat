package rs.app.rsprojekat.model;

//Bcrypt Imports
import at.favre.lib.crypto.bcrypt.BCrypt;

//SQL Imports
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

//JPA Imports
import javax.persistence.*;

@Entity
@Table(name = "korisnik")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "ime", nullable = false, length = 20)
    private String ime;

    @Column(name = "prezime", nullable = false, length = 20)
    private String prezime;

    @Column(name = "email", nullable = false, length = 50, unique = true)
    private String email;

    @Column(name = "pass", nullable = false, length = 250)
    private String pass;

    @Column(name = "wallet", nullable = false)
    private double wallet;

    @Column(name = "admin", nullable = false)
    private boolean admin;

    @Column(name = "organizator", nullable = false)
    private boolean organizator;

    @Column(name = "datum_rod")
    private Date datumRod;

    @Column(name = "approved", nullable = false)
    private boolean approved;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    private boolean isLoggedIn;

    public User() {
        id = 0;
        ime = prezime = email = pass = "";
        wallet = 0.0;
        admin = organizator = approved = false;
        datumRod = Date.valueOf("2000-01-01");
        createdAt = Timestamp.valueOf(LocalDateTime.now());
        isLoggedIn = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        String hashedPassword = BCrypt.withDefaults().hashToString(10, pass.toCharArray());
        this.pass = hashedPassword;
    }

    public double getWallet() {
        return wallet;
    }

    public void setWallet(double wallet) {
        this.wallet = wallet;
    }

    public boolean isOrganizator() {
        return organizator;
    }

    public void setOrganizator(boolean organizator) {
        this.organizator = organizator;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public Date getDatumRod() {
        return datumRod;
    }

    public void setDatumRod(Date datumRod) {
        this.datumRod = datumRod;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }
}