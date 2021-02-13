package com.discordbot.dnd.services.impl;

import com.discordbot.dnd.entities.Inventory;
import com.discordbot.dnd.entities.Pirate;
import com.discordbot.dnd.repositories.InventoryRepository;
import com.discordbot.dnd.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public Inventory getInventoryByPirateId(String pirateId) {
        return inventoryRepository.getInventoryByPirateId(pirateId);
    }

    @Override
    public Inventory create(Pirate pirate) {
        Inventory inventory = new Inventory();
        inventory.setId(UUID.randomUUID().toString());
        inventory.setGold(100);
        inventory.setPirate(pirate);
        return inventoryRepository.save(inventory);
    }
}
