package domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@DiscriminatorValue("GEN") // généraliste par défaut
public class Practicien extends Utilisateur{

    @Column(name = "domaineExpertise", insertable = false, updatable = false)
    private String domaineExpertise;

    @Column(name = "anciennete")
    private Date anciennete;


    @OneToMany(mappedBy = "practicien", cascade = CascadeType.PERSIST)
    private List<RDV> rdv = new ArrayList<RDV>();

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
