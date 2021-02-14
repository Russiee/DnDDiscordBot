package com.discordbot.dnd.listeners.impl;

import com.discordbot.dnd.entities.Work;
import com.discordbot.dnd.exceptions.WorkLogNotExistsException;
import com.discordbot.dnd.exceptions.WorkedTooRecentlyException;
import com.discordbot.dnd.listeners.WorkListener;
import com.discordbot.dnd.services.MessagingService;
import com.discordbot.dnd.services.WorkService;
import com.mysql.cj.protocol.x.XMessageBuilder;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WorkListenerImpl implements WorkListener {
    private static final String WORK = "!work";
    private static final String SHOW_WORK_LIST = "!work list";
    private static final String WORK_FROM_LIST = "(!work)\\s(.*)";

    @Autowired
    private WorkService workService;

    @Autowired
    private MessagingService messagingService;

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        Message message = messageCreateEvent.getMessage();
        String messageContent = message.getContent();
        if (messageCreateEvent.getMessageAuthor().isBotUser()) {
            return;
        }

        //1 - Do work from last logged work
        if (WORK.equalsIgnoreCase(messageContent)) {
            try {
                try {
                    Work workDone = workService.work(message.getAuthor().getIdAsString(), null);
                    String description = "Great job! You've earned " + workDone.getPayout() + " gold for " + workDone.getTimeout() + " minutes of work!";
                    messagingService.sendMessage(messageCreateEvent, "Work", description, null);
                } catch (WorkedTooRecentlyException e) {
                    messagingService.sendMessage(messageCreateEvent,
                            "Work",
                            "Hold your horses! You've worked too recently, have some grog and wait " + e.getTimeLeft() + " minutes!",
                            null);
                }
            } catch (WorkLogNotExistsException e) {
                messagingService.sendMessage(messageCreateEvent,
                        "Work",
                        "You have not chosen a job yet! Choose one from `!work list` and by using `!work <job>`",
                        null);
            }
        }
        //2 - show work list
        else if (SHOW_WORK_LIST.equalsIgnoreCase(messageContent)) {
            List<String> workList = workService.getWorkList();
            String workListString = String.join("\n", workList);
            messagingService.sendMessage(messageCreateEvent, "Work", "Free work available:\n" + workListString, null);
        }
        //3 - do work from list
        else if (messageContent.matches(WORK_FROM_LIST)) {
            String workChoice = messageContent.replace("!work ", "");
            try {
                Work workDone = workService.work(message.getAuthor().getIdAsString(), workChoice);
                String description = "Great job! You've earned " + workDone.getPayout() + " gold for " + workDone.getTimeout() + " minutes of work!";
                messagingService.sendMessage(messageCreateEvent, "Work", description, null);
            } catch (WorkedTooRecentlyException e) {
                messagingService.sendMessage(messageCreateEvent,
                        "Work",
                        "Hold your horses! You've worked too recently, have some grog and wait " + e.getTimeLeft() + " minutes!",
                        null);
            } catch (WorkLogNotExistsException e) {
                messagingService.sendMessage(messageCreateEvent,
                        "Work",
                        "You have not chosen a job yet! Choose one from `!work list` and by using `!work <job>`",
                        null);
            }
        }
    }
}
