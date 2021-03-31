package com.discordbot.dnd.services.impl;

import com.discordbot.dnd.listeners.DeleteListener;
import com.discordbot.dnd.services.MessagingService;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Component
public class MessagingServiceImpl implements MessagingService {

    @Autowired
    private DeleteListener deleteListener;

    @Override
    public CompletableFuture<Message> sendMessage(MessageCreateEvent event, String title, String description, String footer) {
        int red = (int) Math.floor(Math.random() * 255);
        int blue = (int) Math.floor(Math.random() * 255);
        int green = (int) Math.floor(Math.random() * 255);

        if("kuruku".equalsIgnoreCase(event.getMessageAuthor().getDisplayName())) {
            title = title.toUpperCase();
            description = description.toUpperCase();
        }

        return new MessageBuilder().setEmbed(new EmbedBuilder()
                .setAuthor(event.getMessageAuthor())
                .setTitle(title)
                .setDescription(description)
                .setFooter(footer)
                .setColor(new Color(red, green, blue)))
                .send(event.getChannel());
    }

    @Override
    public void sendMessage(MessageCreateEvent event, String title, String description, String footer, boolean withDelete) {
        if(withDelete) {
            this.sendMessage(event, title, description, footer).thenAccept(message -> message.addReactionAddListener(deleteListener).removeAfter(5, TimeUnit.MINUTES));
        } else {
            this.sendMessage(event, title, description, footer);
        }
    }
}
