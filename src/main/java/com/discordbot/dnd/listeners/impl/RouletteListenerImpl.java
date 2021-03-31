package com.discordbot.dnd.listeners.impl;

import com.discordbot.dnd.listeners.RouletteListener;
import com.discordbot.dnd.services.MessagingService;
import com.discordbot.dnd.services.RouletteService;
import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.MessageCreateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RouletteListenerImpl implements RouletteListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(RouletteListenerImpl.class);

    private static final Pattern ROULETTE_BET_PATTERN = Pattern.compile("(!roulette) (\\d*) (\\d+|\\w+)");
    private static final String ROULETTE = "!roulette";
    private static final String ROULETTE_HELP = "!roulette help";


    @Autowired
    private RouletteService rouletteService;

    @Autowired
    private MessagingService messagingService;

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        Message message = messageCreateEvent.getMessage();
        String messageContent = message.getContent().trim();

        if (messageCreateEvent.getMessageAuthor().isBotUser()) {
            return;
        }

        if(messageContent.startsWith(ROULETTE_HELP)) {
            messagingService.sendMessage(messageCreateEvent,
                    "Roulette",
                    "How to play Roulette!\n" +
                            "Spin the wheel and place your bets using:\n" +
                            "`!roulette <bet amount> <even|odd|green|red|black|0-36>`\n" +
                            "For example:\n" +
                            "`!roulette 100 30`\n" +
                            "Or\n" +
                            "`!roulette 50 odd`",
                    null);
        } else if(messageContent.startsWith(ROULETTE) && messageContent.matches(ROULETTE_BET_PATTERN.pattern())) {
            Matcher matcher = ROULETTE_BET_PATTERN.matcher(messageContent);
            List<String> list = new ArrayList<>();
            while (matcher.find()) {
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    list.add(matcher.group(i));
                }
            }
            list.forEach(LOGGER::info);
            Long amount = Long.valueOf(list.get(1));
            String bet = list.get(2);

            rouletteService.play(messageCreateEvent, message.getAuthor().getIdAsString(), amount, bet);
        }
    }
}
