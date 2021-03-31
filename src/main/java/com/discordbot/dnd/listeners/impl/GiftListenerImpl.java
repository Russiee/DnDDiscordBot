package com.discordbot.dnd.listeners.impl;

import com.discordbot.dnd.entities.Pirate;
import com.discordbot.dnd.listeners.GiftListener;
import com.discordbot.dnd.services.InventoryService;
import com.discordbot.dnd.services.MessagingService;
import com.discordbot.dnd.services.PirateService;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class GiftListenerImpl implements GiftListener {
    private static final Pattern GIFT_MATCHER = Pattern.compile("!gift <@!\\d*> (\\d+)");

    @Autowired
    private PirateService pirateService;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private InventoryService inventoryService;

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        if (messageCreateEvent.getMessageAuthor().isBotUser()) {
            return;
        }
        if(messageCreateEvent.getMessageContent().startsWith("!gift")) {
            Matcher matcher = GIFT_MATCHER.matcher(messageCreateEvent.getMessageContent());
            List<String> list = new ArrayList<>();
            while (matcher.find()) {
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    list.add(matcher.group(i));
                }
            }
            Long amount = Long.valueOf(list.get(0));

            List<User> users = messageCreateEvent.getMessage().getMentionedUsers();
            Pirate pirate = pirateService.getPirate(messageCreateEvent.getMessageAuthor().getIdAsString());

            if(pirate.getInventory().getGold() < amount) {
                messagingService.sendMessage(messageCreateEvent, "Gift", "Arr! You lack the funds to give away " + amount + " gold matey!", "You have " + pirate.getInventory().getGold() + " gold.");
                return;
            }
            Pirate gifteePirate = pirateService.getPirate(users.get(0).getIdAsString());
            gifteePirate.getInventory().setGold(gifteePirate.getInventory().getGold() + amount);
            pirate.getInventory().setGold(pirate.getInventory().getGold() - amount);
            inventoryService.update(gifteePirate.getInventory());
            inventoryService.update(pirate.getInventory());

            messagingService.sendMessage(messageCreateEvent, "Gift", "Y'arr...you've taken pity on poor " + gifteePirate.getName() + " and given them " + amount + " gold.", null);
        }
    }

}
