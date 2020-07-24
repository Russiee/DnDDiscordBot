package com.discordbot.dnd.listeners.impl;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.discordbot.dnd.helpers.S3Service;
import com.discordbot.dnd.listeners.SummaryListener;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SummaryListenerImpl implements SummaryListener, MessageCreateListener {
    private static final String LIST_SUMMARY = "!summary list";
    private static final String APPEND_SUMMARY = "!summary append";
    private static final String RM_LINE_SUMMARY = "!summary remove line";
    private static final String SUMMARY_FILE = "data/summary.txt";

    @Value("classpath:data/summary.txt")
    private Resource resourceFile;

    @Autowired
    private S3Service s3Service;

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        System.out.println("receivedMessage from " +
                messageCreateEvent.getMessageAuthor() +
                " from channel " +
                messageCreateEvent.getChannel());

        Message message = messageCreateEvent.getMessage();
        String messageContent = message.getContent();
        if (messageCreateEvent.getMessageAuthor().isBotUser()) {
            return;
        }
        if (messageContent.contains(LIST_SUMMARY)) {
            readSummary(messageCreateEvent);
        } else if (messageContent.contains(APPEND_SUMMARY)) {
            appendSummary(messageCreateEvent);
        } else if (messageContent.contains(RM_LINE_SUMMARY)) {
            removeLineSummary(messageCreateEvent);
        } else if (messageContent.contains("!summary")) {
            sendSummaryHelp(messageCreateEvent);
        }

    }

    private void readSummary(MessageCreateEvent event) {
        try {
            S3Object summary = s3Service.getDocument(SUMMARY_FILE);
            String summaryContent = IOUtils.toString(summary.getObjectContent());
            event.getChannel()
                    .sendMessage(summaryContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void appendSummary(MessageCreateEvent event) {
        try {
            String toAppend = event.getMessage().getContent().replace(APPEND_SUMMARY, "").trim();
            S3Object summary = s3Service.getDocument(SUMMARY_FILE);
            String summaryContent = IOUtils.toString(summary.getObjectContent());
            String summaryToAppend = summaryContent + "\n" + toAppend;
            s3Service.uploadDocument(SUMMARY_FILE, summaryToAppend);
            event.getChannel()
                    .sendMessage("Appended: " + toAppend);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeLineSummary(MessageCreateEvent event) {
        try {
            S3Object summary = s3Service.getDocument(SUMMARY_FILE);
            String summaryContent = IOUtils.toString(summary.getObjectContent());
            StringBuilder sb = new StringBuilder();
            sb.append(summaryContent);
            if (sb.length() > 0) {
                int last, prev = sb.length() - 1;
                while ((last = sb.lastIndexOf("\n", prev)) == prev) {
                    prev = last - 1;
                }
                if (last >= 0) {
                    sb.delete(last, sb.length());
                }
            }

            String summaryToUpload = sb.toString();
            s3Service.uploadDocument(SUMMARY_FILE, summaryToUpload);
            event.getChannel()
                    .sendMessage("Removed previous line in summary. Use !summary list to view changes");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendSummaryHelp(MessageCreateEvent event) {
        new MessageBuilder()
                .append("Uh-oh! Looks like " + event.getMessageAuthor().getDisplayName() + " needs help using !summary")
                .appendNewLine()
                .append("\nThe following commands work with !summary (CaSe-SeNsItIvE)")
                .appendNewLine()
                .appendCode("java", "!summary list")
                .appendNewLine()
                .append("Lists the current summary of the campaign!")
                .appendNewLine()
                .appendCode("java", "!summary append [toAppend]")
                .appendNewLine()
                .append("Will append whatever is inside the brackets to a new line in the current summary.\n" +
                        "Brackets are not required as part of the syntax")
                .appendNewLine()
                .appendCode("java", "!summary remove line")
                .appendNewLine()
                .append("Will remove the last line from the summary. Use sparingly.")
                .appendNewLine()
                .send(event.getChannel());
    }
}
