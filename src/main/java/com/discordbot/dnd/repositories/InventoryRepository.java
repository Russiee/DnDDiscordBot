package com.discordbot.dnd.repositories;

import com.discordbot.dnd.entities.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, String> {

    @Query(nativeQuery = true,
            value = "SELECT * FROM inventory WHERE pirate_id = :id")
    Inventory getInventoryByPirateId(String id);
}
