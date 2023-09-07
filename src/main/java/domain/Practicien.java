package domain;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "domaineExpertise",discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("GEN") // généraliste par défaut
public class Practicien extends Utilisateur{

    @Column(name = "domaineExpertise", insertable = false, updatable = false)
    private String domaineExpertise;

    @Column(name = "anciennete")
    private Date anciennete;

    public Practicien(String name, String tel, Date anciennete) {
        super(name, tel);
        this.anciennete = anciennete;
    }

    public Date getAnciennete() {
        return anciennete;
    }

    public void setAnciennete(Date anciennete) {
        this.anciennete = anciennete;
    }
}
