package com.discordbot.dnd.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "roulette")
public class Roulette {

    @Id
    private String value;

    private String colour;

    public Roulette(String value, String colour) {
        this.value = value;
        this.colour = colour;
    }

    public Roulette() {

    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }
}
