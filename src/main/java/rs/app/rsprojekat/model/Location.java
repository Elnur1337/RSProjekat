package rs.app.rsprojekat.model;

//JPA Imports
import javax.persistence.*;

@Entity
@Table(name = "lokacija")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "naziv", nullable = false, length = 100)
    private String naziv;

    @Column(name = "img_path", nullable = false, length = 200)
    private String imgPath;

    @ManyToOne
    @JoinColumn(name = "id_mjesto", referencedColumnName = "id")
    private Place mjesto;

    public Location() {
        id = 0;
        naziv = imgPath = "";
    }

    public int getId() { return id; }

    public String getNaziv() { return naziv; }

    public void setNaziv(String naziv) { this.naziv = naziv; }

    public String getImgPath() { return imgPath; }

    public void setImgPath(String imgPath) { this.imgPath = imgPath; }
}