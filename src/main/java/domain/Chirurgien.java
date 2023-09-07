package domain;

import jakarta.persistence.Column;

import java.util.Date;

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
