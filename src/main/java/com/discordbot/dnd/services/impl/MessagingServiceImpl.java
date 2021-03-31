package com.discordbot.dnd.services.impl;

import com.discordbot.dnd.services.MessagingService;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class MessagingServiceImpl implements MessagingService {

    @Override
    public void sendMessage(MessageCreateEvent event, String title, String description, String footer) {
        int red = (int) Math.floor(Math.random() * 255);
        int blue = (int) Math.floor(Math.random() * 255);
        int green = (int) Math.floor(Math.random() * 255);

        if("kuruku".equalsIgnoreCase(event.getMessageAuthor().getDisplayName())) {
            title = title.toUpperCase();
            description = description.toUpperCase();
        }

        new MessageBuilder().setEmbed(new EmbedBuilder()
                .setAuthor(event.getMessageAuthor())
                .setTitle(title)
                .setDescription(description)
                .setFooter(footer)
                .setColor(new Color(red, green, blue)))
                .send(event.getChannel());
    }
}
