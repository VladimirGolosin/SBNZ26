package com.ftn.sbnz.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "problems")
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ProblemName name;

    private LocalDate appeared;

    private LocalDate addressed;

    private LocalDate finalized;

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

    public LocalDate getAppeared() {
        return appeared;
    }

    public void setAppeared(LocalDate appeared) {
        this.appeared = appeared;
    }

    public LocalDate getAddressed() {
        return addressed;
    }

    public void setAddressed(LocalDate addressed) {
        this.addressed = addressed;
    }

    public LocalDate getFinalized() {
        return finalized;
    }

    public void setFinalized(LocalDate finalized) {
        this.finalized = finalized;
    }

    public SolutionName getProvidedSolution() {
        return providedSolution;
    }

    public void setProvidedSolution(SolutionName providedSolution) {
        this.providedSolution = providedSolution;
    }
}