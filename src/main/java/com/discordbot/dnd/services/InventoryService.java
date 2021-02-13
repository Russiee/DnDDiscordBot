package com.discordbot.dnd.services;

import com.discordbot.dnd.entities.Inventory;
import com.discordbot.dnd.entities.Pirate;

public interface InventoryService {

    Inventory getInventoryByPirateId(String pirateId);

    Inventory create(Pirate pirate);

    Inventory update(Inventory inventory);
}
