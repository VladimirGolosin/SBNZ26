package com.ftn.sbnz.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "actions")
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ActionName name;

    private Date done;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ActionName getName() {
        return name;
    }

    public void setName(ActionName name) {
        this.name = name;
    }

    public Date getDone() {
        return done;
    }

    public void setDone(Date done) {
        this.done = done;
    }
}