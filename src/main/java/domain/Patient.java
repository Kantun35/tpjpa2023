package domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Patient extends Utilisateur{

    @Column(name = "numero_securite_sociale")
    private String numSecSoc;

    public Patient(String name, String tel, String numSecSoc) {
        super(name, tel);
        this.numSecSoc = numSecSoc;
    }

    public String getNumSecSoc() {
        return numSecSoc;
    }

    public void setNumSecSoc(String numSecSoc) {
        this.numSecSoc = numSecSoc;
    }
}
