package com.discordbot.dnd.listeners.impl;

import com.discordbot.dnd.entities.Pirate;
import com.discordbot.dnd.listeners.PirateListener;
import com.discordbot.dnd.services.MessagingService;
import com.discordbot.dnd.services.PirateService;
import javassist.NotFoundException;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PirateListenerImpl implements PirateListener {

    @Autowired
    private PirateService pirateService;

    @Autowired
    private MessagingService messagingService;

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        Message message = messageCreateEvent.getMessage();
        String messageContent = message.getContent();
        if (messageCreateEvent.getMessageAuthor().isBotUser()) {
            return;
        } else if (messageContent.startsWith("!pirate init")) {
            Pirate pirate = pirateService.getPirate(String.valueOf(message.getAuthor().getId()));
            if (pirate != null) {
                messagingService.sendMessage(messageCreateEvent, "Avast!", "Ye be a pirate already!", null);
                return;
            } else {
                pirate = pirateService.create(String.valueOf(message.getAuthor().getId()), message.getAuthor().getDisplayName());
                String description = "You've been given " + pirate.getInventory().getGold() + " gold for enlisting!";
                messagingService.sendMessage(messageCreateEvent, "Ahoy! Welcome to the crew!", description, null);
            }
        }
    }
}
