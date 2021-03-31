package com.discordbot.dnd.listeners.impl;

import com.discordbot.dnd.entities.Pirate;
import com.discordbot.dnd.listeners.PlunderListener;
import com.discordbot.dnd.services.InventoryService;
import com.discordbot.dnd.services.MessagingService;
import com.discordbot.dnd.services.PirateService;
import org.apache.commons.lang3.time.DateUtils;
import org.javacord.api.entity.message.embed.Embed;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.reaction.ReactionAddListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;

@Component
public class PlunderListenerImpl implements PlunderListener {
    private static Date lastRun = DateUtils.addMinutes(new Date(), -30);
    private static boolean plundered = true;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private PirateService pirateService;

    @Autowired
    private InventoryService inventoryService;

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        if (messageCreateEvent.getMessageContent().equalsIgnoreCase("!plunder")) {

            if (DateUtils.addMinutes(new Date(), -30).after(lastRun) && plundered) {
                plundered = false;
                int randGold = (int) Math.floor(Math.random() * 100);
                messagingService.sendMessage(messageCreateEvent,
                        "Race for the plunder!",
                        "Avast! Plunder has been sighted, the first pirate to react to this wins **" + randGold + "** gold!",
                        "Plunder can be searched for every 30 minutes")
                        .thenAccept(message -> {
                            message.addReactionAddListener(listener -> {
                                if (!plundered) {
                                    Pirate pirate = pirateService.getPirate(listener.getUserIdAsString());
                                    message.edit(new EmbedBuilder()
                                            .setTitle("Race for the plunder!")
                                            .setDescription("Congratulations!" + "\n" + pirate.getName() + " was first and gets " + randGold + " of the plunder!")
                                            .setAuthor(pirate.getName()));
                                    long gold = pirate.getInventory().getGold() + randGold;
                                    pirate.getInventory().setGold(gold);
                                    inventoryService.update(pirate.getInventory());
                                    plundered = true;
                                    lastRun = new Date();
                                }
                            });
                        });
            } else {
                messagingService.sendMessage(messageCreateEvent,
                        "Race for the plunder!",
                        "Hold your horses! You've plundered too recently, wait another " + (int) Duration.between(new Date().toInstant(), DateUtils.addMinutes(lastRun, 30).toInstant()).toMinutes() + " minutes",
                        "Plunder can be searched for every 30 minutes");

            }
        }
    }
}
