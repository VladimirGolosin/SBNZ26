package com.ftn.sbnz.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "problems")
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ProblemName name;

    private Date appeared;

    private Date addressed;

    private Date finalized;

    @Enumerated(EnumType.STRING)
    private SolutionName providedSolution;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProblemName getName() {
        return name;
    }

    public void setName(ProblemName name) {
        this.name = name;
    }

    public Date getAppeared() {
        return appeared;
    }

    public void setAppeared(Date appeared) {
        this.appeared = appeared;
    }

    public Date getAddressed() {
        return addressed;
    }

    public void setAddressed(Date addressed) {
        this.addressed = addressed;
    }

    public Date getFinalized() {
        return finalized;
    }

    public void setFinalized(Date finalized) {
        this.finalized = finalized;
    }

    public SolutionName getProvidedSolution() {
        return providedSolution;
    }

    public void setProvidedSolution(SolutionName providedSolution) {
        this.providedSolution = providedSolution;
    }
}