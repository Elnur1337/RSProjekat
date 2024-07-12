package rs.app.rsprojekat.model;

//SQL Imports
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

//JPA Imports
import javax.persistence.*;

@Entity
@Table(name = "sjedalo")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_sektor", referencedColumnName = "id")
    private Sector sektor;

    public Seat() {
        id = 0;
        sektor = new Sector();
    }

    public int getId() { return id; }

    public Sector getSector() { return sektor; }

    public void setSector(Sector s) { sektor = s; }
}