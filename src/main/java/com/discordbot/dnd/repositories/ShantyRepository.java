package com.discordbot.dnd.repositories;

import com.discordbot.dnd.entities.Shanty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShantyRepository extends JpaRepository<Shanty, String> {

    @Query(nativeQuery = true,
    value = "SELECT *  FROM shanty ORDER BY RAND() LIMIT 1")
    Shanty getShanty();
}
