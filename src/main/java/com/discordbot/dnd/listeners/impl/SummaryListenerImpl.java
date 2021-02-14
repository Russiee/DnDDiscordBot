package com.discordbot.dnd.listeners.impl;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.discordbot.dnd.helpers.S3Service;
import com.discordbot.dnd.listeners.SummaryListener;
import com.discordbot.dnd.services.MessagingService;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SummaryListenerImpl implements SummaryListener, MessageCreateListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(SummaryListenerImpl.class);

    private static final String LIST_SUMMARY = "!summary list";
    private static final String APPEND_SUMMARY = "!summary append";
    private static final String RM_LINE_SUMMARY = "!summary remove line";
    private static final String SUMMARY_FILE = "data/summary.txt";
    private static final String INDEX_ARG_REGEX = "-i\\s*(\\d+)";

    @Value("classpath:data/summary.txt")
    Resource resourceFile;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private MessagingService messagingService;

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        LOGGER.info("receivedMessage from " +
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
            messagingService.sendMessage(event, "Summary", summaryContent, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void appendSummary(MessageCreateEvent event) {
        try {
            String toAppend = event.getMessage().getContent().replace(APPEND_SUMMARY, "").trim();
            S3Object summary = s3Service.getDocument(SUMMARY_FILE);
            String summaryContent = IOUtils.toString(summary.getObjectContent());
            Integer appendIndex = getIndexArgIfPresent(toAppend);
            if (appendIndex != null) {
                toAppend = toAppend.replaceAll(INDEX_ARG_REGEX, "").trim();
                String[] summaryLines = summaryContent.split("\n");
                ArrayList summaryArrayList = new ArrayList(Arrays.asList(summaryLines));
                summaryArrayList.add(appendIndex, toAppend);
                String summaryToUpload = String.join("\n", summaryArrayList);
                s3Service.uploadDocument(SUMMARY_FILE, summaryToUpload);
                messagingService.sendMessage(event, "Summary", String.format("Appended: '%s' at index '%o'", toAppend, appendIndex), null);
            } else {
                String summaryToAppend = summaryContent + "\n" + toAppend;
                s3Service.uploadDocument(SUMMARY_FILE, summaryToAppend);
                messagingService.sendMessage(event, "Summary", String.format("Appended: '%s'", toAppend), null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeLineSummary(MessageCreateEvent event) {
        try {
            String summaryContent = IOUtils.toString(resourceFile.getInputStream());
//            StringBuilder sb = new StringBuilder();
//            sb.append(summaryContent);
//            if (sb.length() > 0) {
//                int last, prev = sb.length() - 1;
//                while ((last = sb.lastIndexOf("\n", prev)) == prev) {
//                    prev = last - 1;
//                }
//                if (last >= 0) {
//                    sb.delete(last, sb.length());
//                }
//            }
//
//            String summaryToUpload = sb.toString();
            String[] summaryLines = summaryContent.split("\n");
            ArrayList summaryArrayList = new ArrayList(Arrays.asList(summaryLines));
            Integer removeIndex = getIndexArgIfPresent(event.getMessage().getContent());
            if (removeIndex != null) {
                summaryArrayList.remove(removeIndex.intValue());
            } else {
                summaryArrayList.remove(summaryLines.length - 1);
            }
            String summaryToUpload = String.join("\n", summaryArrayList);
            s3Service.uploadDocument(SUMMARY_FILE, summaryToUpload);
            event.getChannel()
                    .sendMessage("Removed previous line in summary. Use !summary list to view changes");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Integer getIndexArgIfPresent(String payload) {
        Pattern pattern = Pattern.compile(INDEX_ARG_REGEX);
        Matcher matcher = pattern.matcher(payload);
        if (matcher.find()) {
            return new Integer(matcher.group(1));
        } else {
            return null;
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
                .append("Use the '-i' switch to specify a zero-based index to append/remove to/from the summary.")
                .appendCode("java", "!summary remove line -i 0")
                .send(event.getChannel());
    }
}
