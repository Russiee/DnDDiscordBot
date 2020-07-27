package com.discordbot.dnd.repositories;

import com.discordbot.dnd.entities.Handout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HandoutRepository extends JpaRepository<Handout, String> {

}
