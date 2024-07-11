package rs.app.rsprojekat.model;

import javax.persistence.*;

@Entity
@Table(name = "sektor")
public class Sector {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "naziv", nullable = false, length = 100)
    private String naziv;

    @Column(name = "kapacitet", nullable = false)
    private int kapacitet;

    @ManyToOne
    @JoinColumn(name = "id_lokacija", referencedColumnName = "id")
    private Location lokacija;

    public Sector() {
        naziv = "";
        kapacitet = 0;
        lokacija = new Location();
    }

    public int getId() {
        return id;
    }

    public String getNaziv() {
        return naziv;
    }

    public int getKapacitet() {
        return kapacitet;
    }

    public Location getLokacija() {
        return lokacija;
    }

    public void setNaziv(String nazivInput) {
        naziv = nazivInput;
    }

    public void setKapacitet(int kapacitetInput) {
        kapacitet = kapacitetInput;
    }

    public void setLokacija(Location lokacijaInput) {
        lokacija = lokacijaInput;
    }
}
