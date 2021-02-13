package com.discordbot.dnd.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "work_log")
public class WorkLog {

    @Id
    private String id;

    @ManyToOne(cascade= CascadeType.PERSIST, fetch=FetchType.EAGER)
    @JoinColumn(name="work_id", referencedColumnName="id")
    private Work work;

    @OneToOne(cascade=CascadeType.PERSIST, fetch=FetchType.EAGER)
    @JoinColumn(name="pirate_id", referencedColumnName="id")
    private Pirate pirate;

    private Date date;

    public WorkLog(String id, Work work, Pirate pirate, Date date) {
        this.id = id;
        this.work = work;
        this.pirate = pirate;
        this.date = date;
    }

    public WorkLog() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }

    public Pirate getPirate() {
        return pirate;
    }

    public void setPirate(Pirate pirate) {
        this.pirate = pirate;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
