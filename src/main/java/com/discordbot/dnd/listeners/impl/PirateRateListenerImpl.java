package com.discordbot.dnd.listeners.impl;

import com.discordbot.dnd.listeners.PirateRateListener;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class PirateRateListenerImpl implements PirateRateListener {

    private static final String PIRATE_EMOJI = "\uD83C\uDFF4\u200D";
    private static final String SKULL_CROSSBONES = "☠️";

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        if (messageCreateEvent.getMessageAuthor().isBotUser()) {
            return;
        } else if (messageCreateEvent.getMessageContent().contains("!pirateRate")) {
            int pirateRate = (int) Math.floor(Math.random() * 100) + 1;
            String title = "";
            String message = "";
            int red = (int) Math.floor(Math.random() * 255);
            int blue = (int) Math.floor(Math.random() * 255);
            int green = (int) Math.floor(Math.random() * 255);
            if (pirateRate <= 1) {
                title = "Stowaway!!";
                message = "Throw him overboard! They're **" + pirateRate + "%** pirate!!";
            } else if (pirateRate <= 10) {
                title = "Yarr you be walking the plank!";
                message = "Wow you're only **" + pirateRate + "%** pirate...";
            } else if (pirateRate <= 49) {
                title = "Swab the poop deck!";
                message = "Ye be a youngin' **" + pirateRate + "%** pirate with lots to learn";
            } else if (pirateRate <= 79) {
                title = "Aye aye cap'n";
                message = "Ahoy matey! Truly a seasoned **" + pirateRate + "%** pirate!";
            } else if (pirateRate <= 98) {
                title = "Truly most fearsome";
                message = "Worthy of the same ranks of Black Bart and Blackbeard you **" + pirateRate + "%** pirate!";
            } else if (pirateRate <= 100) {
                title = "Ching Shih";
                message = "The fiercest **" + pirateRate + "%** pirate to ever sail the seven seas!";
            }
            new MessageBuilder().setEmbed(new EmbedBuilder()
                    .setAuthor(messageCreateEvent.getMessageAuthor())
                    .setTitle(title)
                    .setDescription(message)
                    .setFooter(PIRATE_EMOJI + SKULL_CROSSBONES)
                    .setColor(new Color(red, green, blue)))
                    .send(messageCreateEvent.getChannel());
        }
    }
}
