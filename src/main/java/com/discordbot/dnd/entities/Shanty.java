package com.discordbot.dnd.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "shanty")
public class Shanty {

    @Id
    private String title;

    private String content;

    public Shanty(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Shanty() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
