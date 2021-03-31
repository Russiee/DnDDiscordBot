package com.discordbot.dnd.services;

import org.javacord.api.event.message.MessageCreateEvent;

public interface RouletteService {

    void play(MessageCreateEvent messageCreateEvent, String pirateId, Long amount, String bet);
}
