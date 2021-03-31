package com.discordbot.dnd.services.impl;

import com.discordbot.dnd.entities.Pirate;
import com.discordbot.dnd.entities.Roulette;
import com.discordbot.dnd.repositories.RouletteRepository;
import com.discordbot.dnd.services.InventoryService;
import com.discordbot.dnd.services.MessagingService;
import com.discordbot.dnd.services.PirateService;
import com.discordbot.dnd.services.RouletteService;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Component
public class RouletteServiceImpl implements RouletteService {
    private static final String ROULETTE = "Roulette";
    private static final Pattern PERMITTED_BETS = Pattern.compile("(even|odd|green|red|black|\\b(0?[0-9]|1[0-9]|2[0-9]|3[0-6])\\b)");
    private static final List<String> betType = List.of("odd", "even", "red", "black", "green");

    @Autowired
    private RouletteRepository rouletteRepository;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private PirateService pirateService;

    @Autowired
    private InventoryService inventoryService;

    @Override
    public void play(MessageCreateEvent messageCreateEvent, String pirateId, Long amount, String bet) {
        Pirate pirate = pirateService.getPirate(pirateId);

        if(pirate.getInventory().getGold() < amount) {
            messagingService.sendMessage(messageCreateEvent, ROULETTE, "Think you're being clever do you?\n You can't afford that much!", "You have: " + pirate.getInventory().getGold() + " gold.");
            return;
        }

        if(!bet.matches(PERMITTED_BETS.pattern())) {
            messagingService.sendMessage(messageCreateEvent, ROULETTE, "Pick one of:\nType: `red | black | green`\nNumbers between: `0 and 36`", "You picked: " + bet);
            return;
        }

        long payoutAmount = 0;
        pirate.getInventory().setGold(pirate.getInventory().getGold() - amount);
        inventoryService.update(pirate.getInventory());
        int spin = (int) Math.floor(Math.random() * 37);
        Roulette roulette = rouletteRepository.getRouletteById(String.valueOf(spin));
        if(betType.contains(bet.toLowerCase())) {
            payoutAmount = amount * 2;
            if(bet.equalsIgnoreCase("odd")) {
                if(spin % 2 == 1) {
                    success(messageCreateEvent, bet, pirate, payoutAmount, roulette);
                } else {
                    fail(messageCreateEvent, bet, pirate, payoutAmount, roulette, pirate.getInventory().getGold());
                }
            } else if(bet.equalsIgnoreCase("even")) {
                if(spin % 2 == 0) {
                    success(messageCreateEvent, bet, pirate, payoutAmount, roulette);
                } else {
                    fail(messageCreateEvent, bet, pirate, payoutAmount, roulette, pirate.getInventory().getGold());

                }
            } else {
                if(roulette.getColour().equalsIgnoreCase(bet)) {
                    if("green".equals(bet)) {
                        payoutAmount = amount * 35;
                    }
                    success(messageCreateEvent, bet, pirate, payoutAmount, roulette);
                } else {
                    fail(messageCreateEvent, bet, pirate, payoutAmount, roulette, pirate.getInventory().getGold());
                }
            }
        } else {
            payoutAmount = amount * 35;
            if(roulette.getValue().equalsIgnoreCase(bet)) {
                success(messageCreateEvent, bet, pirate, payoutAmount, roulette);
            } else {
                fail(messageCreateEvent, bet, pirate, payoutAmount, roulette, pirate.getInventory().getGold());
            }
        }
    }

    private void fail(MessageCreateEvent messageCreateEvent, String bet, Pirate pirate, long payoutAmount, Roulette roulette, long l) {
        pirate.getInventory().setGold(l);
        inventoryService.update(pirate.getInventory());
        sendMessage(messageCreateEvent, roulette, payoutAmount, bet, false);
    }

    private void success(MessageCreateEvent messageCreateEvent, String bet, Pirate pirate, long payoutAmount, Roulette roulette) {
        long gold = pirate.getInventory().getGold() + payoutAmount;
        pirate.getInventory().setGold(gold);
        inventoryService.update(pirate.getInventory());
        sendMessage(messageCreateEvent, roulette, payoutAmount, bet, true);
    }

    private void sendMessage(MessageCreateEvent messageCreateEvent, Roulette spin, long payout, String bet, boolean success) {
        if(success) {
            messagingService.sendMessage(messageCreateEvent, ROULETTE, "You win!\nSpin: " + spin.getValue()  + "\nColour: " + spin.getColour() + "\nYou bet it would be: " + bet + " and won " + payout + " gold!", "Spin again?");
        } else {
            messagingService.sendMessage(messageCreateEvent, ROULETTE, "Bad luck!\nSpin: " + spin.getValue() + "\nColour: " + spin.getColour() + "\nYou bet it would be: " + bet, "Spin again?");
        }
    }
}
