package com.discordbot.dnd.listeners.impl;

import com.discordbot.dnd.entities.Handout;
import com.discordbot.dnd.listeners.HandoutListener;
import com.discordbot.dnd.services.HandoutService;
import javassist.NotFoundException;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class HandoutListenerImpl implements HandoutListener, MessageCreateListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(HandoutListenerImpl.class);
    private static final Pattern ADD_HANDOUT_PATTERN = Pattern.compile("(!handout) (\\w+) (.+)");
    private static final Pattern GET_HANDOUT_PATTERN = Pattern.compile("(!handout) \\b(\\S+)$");

    @Autowired
    private HandoutService handoutService;

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        Message message = messageCreateEvent.getMessage();
        String messageContent = message.getContent().trim();

        if (Pattern.matches(ADD_HANDOUT_PATTERN.pattern(), messageContent)) {
            Matcher matcher = ADD_HANDOUT_PATTERN.matcher(messageContent);
            List<String> list = new ArrayList<>();
            while (matcher.find()) {
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    list.add(matcher.group(i));
                }
            }
            list.forEach(LOGGER::info);
            String name = list.get(1);
            String url = list.get(2);
            LOGGER.info("Creating handout with name " + name + " and url " + url);
            handoutService.saveHandout(name, url);
            messageCreateEvent.getChannel().sendMessage("Successfully saved new handout: " + name);
        } else if (Pattern.matches(GET_HANDOUT_PATTERN.pattern(), messageContent)) {
            Matcher matcher = GET_HANDOUT_PATTERN.matcher(messageContent);
            List<String> list = new ArrayList<>();
            while (matcher.find()) {
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    list.add(matcher.group(i));
                }
            }
            list.forEach(LOGGER::info);
            String name = list.get(1);
            try {
                Handout handout = handoutService.getHandoutByName(name);
                MessageBuilder builder = new MessageBuilder()
                        .append("Here is the ")
                        .append(handout.getName())
                        .append(" handout: ")
                        .appendNewLine()
                        .addAttachment(new URL(handout.getUrl()));
                builder.send(messageCreateEvent.getChannel());
            } catch (NotFoundException | MalformedURLException e) {
                messageCreateEvent.getChannel()
                        .sendMessage("Could not find Handout with name " + name);
            }
        } else if (messageContent.contains("!handout")) {
            MessageBuilder messageBuilder = new MessageBuilder()
                    .append("Oops! It looks like ")
                    .append(messageCreateEvent.getMessageAuthor().getDisplayName())
                    .append(" is having trouble using handouts")
                    .appendNewLine()
                    .appendCode("java", "!handout [name] [url]")
                    .appendNewLine()
                    .append("Use this syntax to upload a new handout. The name is required to be unique and not used")
                    .appendNewLine()
                    .append("If the name already exists as a handout the url will be overridden")
                    .appendNewLine()
                    .append("and the url must link to the handout you want to display.")
                    .appendNewLine()
                    .appendCode("java", "!handout [name]")
                    .appendNewLine()
                    .append("Without the url, the command will be used to display the url of the handout itself.");
            messageBuilder.send(messageCreateEvent.getChannel());
        }
    }
}
