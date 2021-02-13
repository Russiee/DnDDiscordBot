package com.discordbot.dnd.entities;

import javax.persistence.*;

@Entity
@Table(name = "pirate")
public class Pirate {

    @Id
    private String id;

    private String name;

    @OneToOne(cascade=CascadeType.PERSIST, fetch=FetchType.EAGER)
    @JoinColumn(name="inventory_id", referencedColumnName="id")
    private Inventory inventory;

    public Pirate(String id, String name, Inventory inventory) {
        this.id = id;
        this.name = name;
        this.inventory = inventory;
    }

    public Pirate() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
