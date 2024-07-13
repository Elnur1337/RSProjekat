package rs.app.rsprojekat.model;

//SQL Imports
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

//JPA Imports
import javax.persistence.*;

@Entity
@Table(name = "karta")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "cijena", nullable = false)
    private double price;

    @Column(name = "bought", nullable = false)
    private boolean bought;

    @Column(name = "reserved", nullable = false)
    private boolean reserved;

    @Column(name = "reserved_to", nullable = true)
    private Date reservedTo;

    @ManyToOne
    @JoinColumn(name = "id_dogadjaj", referencedColumnName = "id")
    private Dogadjaj dogadjaj;

    @ManyToOne
    @JoinColumn(name = "id_kupac", referencedColumnName = "id")
    private User kupac;

    @ManyToOne
    @JoinColumn(name = "id_sjedalo", referencedColumnName = "id")
    private Seat sjedalo;

    public Ticket() {
        id = 0;
        bought = reserved = false;
        reservedTo = null;
    }

    public int getId() { return id; }

    public double getPrice() { return price; }

    public void setPrice(double price) { this.price = price; }

    public boolean getBought() { return bought; }

    public void setBought(boolean bought) { this.bought = bought; }

    public boolean getReserved() { return reserved; }

    public void setReserved(boolean reserved) { this.reserved = reserved; }

    public Date getReservedTo() { return reservedTo; }

    public void setReservedTo(Date reservedTo) { this.reservedTo = reservedTo; }

    public Dogadjaj getDogadjaj() { return dogadjaj; }

    public void setDogadjaj(Dogadjaj dogadjaj) { this.dogadjaj = dogadjaj; }

    public User getKupac() { return kupac; }

    public void setKupac(User kupac) { this.kupac = kupac; }

    public Seat getSjedalo() { return sjedalo; }

    public void setSjedalo(Seat sjedalo) { this.sjedalo = sjedalo; }
}