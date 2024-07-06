package rs.app.rsprojekat.model;

//SQL Imports
import java.sql.Timestamp;
import java.time.LocalDateTime;

//JPA Imports
import javax.persistence.*;

@Entity
@Table(name = "kategorija")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "naziv", nullable = false, length = 100)
    private String naziv;

    public Category() {
        id = 0;
        naziv = "";
    }

    public int getId() { return id; }

    public String getNaziv() { return naziv; }

    public void setNaziv(String naziv) { this.naziv = naziv; }
}