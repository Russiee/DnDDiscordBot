package com.discordbot.dnd.services;

import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.MessageCreateEvent;

import java.util.concurrent.CompletableFuture;

public interface MessagingService {

    CompletableFuture<Message> sendMessage(MessageCreateEvent event, String title, String description, String footer);
    void sendMessage(MessageCreateEvent event, String title, String description, String footer, boolean withDelete);

}
