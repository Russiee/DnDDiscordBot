package com.discordbot.dnd.services;

import com.discordbot.dnd.entities.Pirate;
import javassist.NotFoundException;

public interface PirateService {
    Pirate getPirate(String id);

    Pirate create(String id, String name);

    Pirate update(Pirate pirate);
}
