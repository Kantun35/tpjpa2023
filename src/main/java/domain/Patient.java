package domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("PAT") // généraliste par défaut
public class Patient extends Utilisateur{

    @Column(name = "numero_securite_sociale")
    private String numSecSoc;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.PERSIST)
    private List<RDV> rdv = new ArrayList<RDV>();

    public Patient(String name, String tel, String numSecSoc) {
        super(name, tel);
        this.numSecSoc = numSecSoc;
    }

    public Patient() {
        super();

    }

    public String getNumSecSoc() {
        return numSecSoc;
    }

    public void setNumSecSoc(String numSecSoc) {
        this.numSecSoc = numSecSoc;
    }
}
