package com.discordbot.dnd.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "handout")
public class Handout {

    @Id
    private String name;

    private String url;

    public Handout(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public Handout() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
