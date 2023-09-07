package domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.Date;

@Entity
@DiscriminatorValue("KIN")
public class Kine extends Practicien{

    @Column(name = "colonne_cassée_en_deux")
    private int colonneCasseeEnDeux;

    public Kine(String name, String tel, Date anciennete, int colonneCasseeEnDeux) {
        super(name, tel, anciennete);
        this.colonneCasseeEnDeux = colonneCasseeEnDeux;
    }

    public int getColonneCasseeEnDeux() {
        return colonneCasseeEnDeux;
    }

    public void setColonneCasseeEnDeux(int colonneCasseeEnDeux) {
        this.colonneCasseeEnDeux = colonneCasseeEnDeux;
    }
}
