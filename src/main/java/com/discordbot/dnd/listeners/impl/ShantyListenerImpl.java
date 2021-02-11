package com.discordbot.dnd.listeners.impl;

import com.discordbot.dnd.listeners.ShantyListener;
import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringWriter;

@Component
public class ShantyListenerImpl implements ShantyListener {

    @Value("classpath:data/shanty.txt")
    Resource resourceFile;

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        Message message = messageCreateEvent.getMessage();
        String messageContent = message.getContent();
        if (messageCreateEvent.getMessageAuthor().isBotUser()) {
            return;
        } else if(messageContent.contains("!shanty")) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(resourceFile.getFile()));

                String line = null;
                int randNum = (int) Math.floor(Math.random() * 34);
                int numOfLines = 0;
                while ((line = reader.readLine()) != null){
                    if(numOfLines == randNum) {
                        line = line.replaceAll("[\\\\][n]", "\n");
                        messageCreateEvent.getChannel()
                                .sendMessage(line);
                    }
                    numOfLines = numOfLines+1;
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
