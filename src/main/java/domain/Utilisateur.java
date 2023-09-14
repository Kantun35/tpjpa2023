package domain;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity(name = "Utilisateur") // pas nécessaire de renommer si tu veux garder le nom
@Inheritance(strategy= InheritanceType.SINGLE_TABLE) // regroupe dans cette même table les attributs des classe enfants
@DiscriminatorColumn(name = "domaineExpertise",discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("USE") // généraliste par défaut
public class Utilisateur {

    @Id
    @GeneratedValue
    @Column(name = "id", unique = true)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "telephone")
    private String tel;

    public Utilisateur(String name, String tel) {
        this.name = name;
        this.tel = tel;
    }

    public Utilisateur() {

    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return this.tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Override
    public String toString() {
        return "Utilisateur [id=" + id + ", name=" + name + ", tel=" + tel + "]\n";
    }
}
