package domain;

import jakarta.persistence.*;

import java.util.Date;

@Entity(name = "RDV") // pas nécessaire de renommer si tu veux garder le nom
public class RDV {

    @Id
    @GeneratedValue
    @Column(name = "id", unique = true)
    private long id;

    @Column(name = "date")
    private Date date ;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Patient patient;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Practicien practicien;

    @Column(name= "etat")
    private String etat;

    public RDV(Date date, Patient patient, Practicien practicien, String etat) {
        this.date = date;
        this.patient = patient;
        this.practicien = practicien;
        this.etat = etat;
    }

    public long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Practicien getPracticien() {
        return practicien;
    }

    public void setPracticien(Practicien practicien) {
        this.practicien = practicien;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    @Override
    public String toString() {
        return "RDV [id=" + this.id + "]\n";
    }
}
