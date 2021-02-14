package com.discordbot.dnd.services;

import org.javacord.api.event.message.MessageCreateEvent;

public interface MessagingService {

    void sendMessage(MessageCreateEvent event, String title, String description, String footer);
}
