package com.discordbot.dnd.repositories;

import com.discordbot.dnd.entities.Roulette;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RouletteRepository extends JpaRepository<Roulette, String> {

    @Query(nativeQuery = true,
    value = "SELECT * FROM roulette WHERE value = :id")
    Roulette getRouletteById(String id);
}
