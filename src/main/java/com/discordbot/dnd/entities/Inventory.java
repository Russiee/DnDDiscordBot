package com.discordbot.dnd.entities;

import javax.persistence.*;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    private String id;

    private long gold;

    @OneToOne(cascade=CascadeType.PERSIST, fetch=FetchType.EAGER)
    @JoinColumn(name="pirate_id", referencedColumnName="id")
    private Pirate pirate;

    public Inventory(String id, long gold, Pirate pirate) {
        this.id = id;
        this.gold = gold;
        this.pirate = pirate;
    }

    public Inventory() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getGold() {
        return gold;
    }

    public void setGold(long gold) {
        this.gold = gold;
    }

    public Pirate getPirate() {
        return pirate;
    }

    public void setPirate(Pirate pirate) {
        this.pirate = pirate;
    }
}
