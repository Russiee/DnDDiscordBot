package com.discordbot.dnd.services.impl;

import com.discordbot.dnd.entities.Shanty;
import com.discordbot.dnd.repositories.ShantyRepository;
import com.discordbot.dnd.services.ShantyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShantyServiceImpl implements ShantyService {

    @Autowired
    private ShantyRepository shantyRepository;

    @Override
    public Shanty getRandomShanty() {
        return shantyRepository.getShanty();
    }
}
