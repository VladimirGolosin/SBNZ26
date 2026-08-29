package com.ftn.sbnz.model;

import javax.persistence.*;

@Entity
@Table(name = "culture_stats")
public class CultureStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CultureName name;

    private int level;

    private int size;

    private int number;

    private double spentOnActions;

    private double spentOnProblems;

    private double sellingPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CultureName getName() {
        return name;
    }

    public void setName(CultureName name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getSpentOnActions() {
        return spentOnActions;
    }

    public void setSpentOnActions(double spentOnActions) {
        this.spentOnActions = spentOnActions;
    }

    public double getSpentOnProblems() {
        return spentOnProblems;
    }

    public void setSpentOnProblems(double spentOnProblems) {
        this.spentOnProblems = spentOnProblems;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
}
