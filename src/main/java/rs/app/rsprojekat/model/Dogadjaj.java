package rs.app.rsprojekat.model;

//SQL Imports
import java.sql.Timestamp;
import java.time.LocalDateTime;

//JPA Imports
import javax.persistence.*;

@Entity
@Table(name = "dogadjaj")
public class Dogadjaj {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "naziv", nullable = false, length = 150)
    private String naziv;

    @Column(name = "opis", nullable = false, length = 500)
    private String opis;

    @Column(name = "img_path", nullable = false, length = 200)
    private String imgPath;

    @Column(name = "start_date", nullable = false)
    private Timestamp startDate;

    @Column(name = "end_date", nullable = false)
    private Timestamp endDate;

    @Column(name = "base_price", nullable = false)
    private double basePrice;

    @Column(name = "available", nullable = false)
    private boolean available;

    @Column(name = "approved", nullable = false)
    private boolean approved;

    @ManyToOne
    @JoinColumn(name = "id_organizator", referencedColumnName = "id")
    private User organizator;

    @ManyToOne
    @JoinColumn(name = "id_podkategorija", referencedColumnName = "id")
    private Subcategory podkategorija;

    @ManyToOne
    @JoinColumn(name = "id_lokacija", referencedColumnName = "id")
    private Location lokacija;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @OneToOne
    @JoinColumn(name = "id_original", nullable = true)
    private Dogadjaj original;

    public Dogadjaj() {
        id = 0;
        naziv = opis = "";
        available = approved = false;
        startDate = endDate = createdAt = Timestamp.valueOf(LocalDateTime.now());
        original = null;
    }

    public Dogadjaj(Dogadjaj noviDogadjaj) {
        this.naziv = noviDogadjaj.naziv;
        this.opis = noviDogadjaj.opis;
        this.imgPath = noviDogadjaj.imgPath;
        this.startDate = noviDogadjaj.startDate;
        this.endDate = noviDogadjaj.endDate;
        this.basePrice = noviDogadjaj.basePrice;
        this.available = this.approved = false;
        this.organizator = noviDogadjaj.organizator;
        this.podkategorija = noviDogadjaj.podkategorija;
        this.lokacija = noviDogadjaj.lokacija;
        this.createdAt = noviDogadjaj.createdAt;
    }

    public int getId() {
        return id;
    }

    public String getNaziv() { return naziv; }

    public void setNaziv(String naziv) { this.naziv = naziv; }

    public String getOpis() { return opis; }

    public void setOpis(String opis) { this.opis = opis; }

    public String getImgPath() { return imgPath; }

    public void setImgPath(String imgPath) { this.imgPath = imgPath; }

    public Timestamp getStartDate() { return startDate; }

    public void setStartDate(Timestamp startDate) { this.startDate = startDate; }

    public Timestamp getEndDate() { return endDate; }

    public void setEndDate(Timestamp endDate) { this.endDate = endDate; }

    public double getBasePrice() { return basePrice; }

    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }

    public boolean isAvailable() { return available; }

    public void setAvailable(boolean available) { this.available = available; }

    public boolean isApproved() { return approved; }

    public void setApproved(boolean approved) { this.approved = approved; }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setOrganizator(User organizator) { this.organizator = organizator; }

    public User getOrganizator() { return organizator; }

    public void setPodkategorija(Subcategory podkategorija) { this.podkategorija = podkategorija; }

    public Subcategory getPodkategorija() { return podkategorija; }

    public void setLokacija(Location lokacija) { this.lokacija = lokacija; }

    public Location getLokacija() { return lokacija; }

    public void setOriginal(Dogadjaj d) { this.original = d; }

    public Dogadjaj getOriginal() { return original; }

    @Override
    public String toString() {
        return "ID: " + id + "\nNaziv: " + naziv + "\nMjesto: " + lokacija.getMjesto().getNaziv() + "\nLokacija: " + lokacija.getNaziv() + "\nImage path: " + imgPath + "\nLocation path: " + lokacija.getImgPath();
    }
}