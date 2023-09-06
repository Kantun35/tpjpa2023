package domain;

import jakarta.persistence.*;


@Entity
@DiscriminatorValue("ADM")
public class Administrateur extends Employee {


    @Column
    private String mdpEnClaire;


    public Administrateur() {
    }

    public Administrateur( String name ,Department department,String mdpEnClaire) {
        super(name,department);
        this.mdpEnClaire = mdpEnClaire;
    }

    public String getMdpEnClaire() {
        return mdpEnClaire;
    }

    public void setMdpEnClaire(String mdpEnClaire) {
        this.mdpEnClaire = mdpEnClaire;
    }

    @Override
    public String toString() {
        return "Employee [id=" + this.getId() + ", name=" + this.getName() + ", department="
                + this.getDepartment().getName() + ", mdp=" + this.getMdpEnClaire() + "]\n";
    }
}
