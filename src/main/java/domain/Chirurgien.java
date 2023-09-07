package domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.Date;

@Entity
@DiscriminatorValue("CHI")
public class Chirurgien  extends Practicien {

    @Column(name = "opération_ratée")
    private int operationRatee;

    public Chirurgien(String name, String tel, Date anciennete, int operationRatee) {
        super(name, tel, anciennete);
        this.operationRatee = operationRatee;
    }

    public int getOperationRatee() {
        return operationRatee;
    }

    public void setOperationRatee(int operationRatee) {
        this.operationRatee = operationRatee;
    }
}
