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

    @Column(name = "id_organizator", nullable = false)
    private int idOrganizator;

    @Column(name = "id_podkategorija", nullable = false)
    private int idPodkategorija;

    @Column(name = "id_lokacija", nullable = false)
    private int idLokacija;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    public Dogadjaj() {
        id = 0;
        naziv = opis = "";
        available = approved = false;
        startDate = endDate = createdAt = Timestamp.valueOf(LocalDateTime.now());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}