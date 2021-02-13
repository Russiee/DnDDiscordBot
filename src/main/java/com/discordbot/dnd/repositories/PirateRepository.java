package com.discordbot.dnd.repositories;

import com.discordbot.dnd.entities.Pirate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PirateRepository extends JpaRepository<Pirate, String> {
    Optional<Pirate> findPirateById(String id);
}
