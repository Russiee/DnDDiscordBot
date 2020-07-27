package com.discordbot.dnd.services.impl;

import com.discordbot.dnd.entities.Handout;
import com.discordbot.dnd.repositories.HandoutRepository;
import com.discordbot.dnd.services.HandoutService;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class HandoutServiceImpl implements HandoutService {
    private final static Logger LOGGER = LoggerFactory.getLogger(HandoutServiceImpl.class);

    @Autowired
    private HandoutRepository handoutRepository;

    @Override
    public Handout getHandoutByName(String name) throws NotFoundException {
        LOGGER.info("Retrieving handout with name " + name);
        Optional<Handout> handoutOpt = handoutRepository.findById(name);
        return handoutOpt.orElseThrow(() -> new NotFoundException("Handout does not exist"));
    }

    @Override
    public void saveHandout(String name, String url) {
        Optional<Handout> handoutOpt = handoutRepository.findById(name);
        handoutOpt.ifPresentOrElse(handout -> {
            handout.setUrl(url);
            handoutRepository.saveAndFlush(handout);
        }, () -> {
            Handout handout = new Handout(name, url);
            LOGGER.info("Saving new handout with name " + name + " and url " + url);
            handoutRepository.saveAndFlush(handout);
        });
    }
}
