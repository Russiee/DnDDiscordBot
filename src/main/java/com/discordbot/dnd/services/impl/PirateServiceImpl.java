package com.discordbot.dnd.services.impl;

import com.discordbot.dnd.entities.Pirate;
import com.discordbot.dnd.repositories.PirateRepository;
import com.discordbot.dnd.services.InventoryService;
import com.discordbot.dnd.services.PirateService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PirateServiceImpl implements PirateService {

    @Autowired
    private PirateRepository pirateRepository;

    @Autowired
    private InventoryService inventoryService;

    @Override
    public Pirate getPirate(String id) {
        Optional<Pirate> pirateOpt = pirateRepository.findPirateById(id);
        return pirateOpt.orElse(null);
    }

    @Override
    public Pirate create(String id, String name) {
        Pirate pirate = new Pirate();
        pirate.setId(id);
        pirate = pirateRepository.saveAndFlush(pirate);

        pirate.setName(name);
        pirate.setInventory(inventoryService.create(pirate));
        return pirateRepository.save(pirate);
    }

    @Override
    public Pirate update(Pirate pirate) {
        return pirateRepository.saveAndFlush(pirate);
    }


}
