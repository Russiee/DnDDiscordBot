package com.discordbot.dnd.listeners.impl;

import com.discordbot.dnd.listeners.SummaryListener;
import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;

@Component
public class SummaryListenerImpl implements SummaryListener, MessageCreateListener {

    @Value("classpath:data/summary.txt")
    Resource resourceFile;

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        System.out.println("receivedMessage from " +
                messageCreateEvent.getMessageAuthor() +
                " from channel " +
                messageCreateEvent.getChannel());

        Message message = messageCreateEvent.getMessage();
        if (message.getContent().equalsIgnoreCase("!summary")) {
            try {
                messageCreateEvent.getChannel()
                        .sendMessage(
                                new String(
                                        Files.readAllBytes(resourceFile.getFile()
                                                .toPath())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
