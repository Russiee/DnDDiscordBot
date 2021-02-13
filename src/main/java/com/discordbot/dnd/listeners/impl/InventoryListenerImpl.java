package com.discordbot.dnd.listeners.impl;

import com.discordbot.dnd.entities.Inventory;
import com.discordbot.dnd.entities.Pirate;
import com.discordbot.dnd.listeners.InventoryListener;
import com.discordbot.dnd.services.InventoryService;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InventoryListenerImpl implements InventoryListener {

    @Autowired
    private InventoryService inventoryService;

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        Message message = messageCreateEvent.getMessage();
        String messageContent = message.getContent();
        if (messageCreateEvent.getMessageAuthor().isBotUser()) {
            return;
        } else if (messageContent.startsWith("!inventory")) {
            Inventory inventory = inventoryService.getInventoryByPirateId(String.valueOf(message.getAuthor().getId()));
            new MessageBuilder().setEmbed(new EmbedBuilder()
                    .setTitle("Inventory")
                    .setDescription("You've " + inventory.getGold() + " gold!")
                    .setAuthor(message.getAuthor()))
                    .send(messageCreateEvent.getChannel());
        }
    }
}
