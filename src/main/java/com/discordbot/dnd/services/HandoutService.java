package com.discordbot.dnd.services;

import com.discordbot.dnd.entities.Handout;
import javassist.NotFoundException;

public interface HandoutService {

    Handout getHandoutByName(String name) throws NotFoundException;

    void saveHandout(String name, String url);
}
