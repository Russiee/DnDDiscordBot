package com.discordbot.dnd.listeners.impl;

import com.discordbot.dnd.entities.Shanty;
import com.discordbot.dnd.listeners.ShantyListener;
import com.discordbot.dnd.services.MessagingService;
import com.discordbot.dnd.services.ShantyService;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringWriter;

@Component
public class ShantyListenerImpl implements ShantyListener {

    @Autowired
    private ShantyService shantyService;

    @Autowired
    private MessagingService messagingService;

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        Message message = messageCreateEvent.getMessage();
        String messageContent = message.getContent();
        if (messageCreateEvent.getMessageAuthor().isBotUser()) {
            return;
        } else if (messageContent.contains("!shanty")) {
            Shanty shanty = shantyService.getRandomShanty();
            String shantyContent = shanty.getContent().replaceAll("[\\\\][n]", "\n");
            if (messageContent.contains("-kuruku")) {
                shantyContent = shantyContent.toUpperCase();
            }

            messagingService.sendMessage(messageCreateEvent, shanty.getTitle(), shantyContent, null);
        }
    }
}
