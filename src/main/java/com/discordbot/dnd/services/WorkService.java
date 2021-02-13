package com.discordbot.dnd.services;

import com.discordbot.dnd.entities.Work;
import com.discordbot.dnd.exceptions.WorkLogNotExistsException;
import com.discordbot.dnd.exceptions.WorkedTooRecentlyException;

import java.util.List;

public interface WorkService {
    List<String> getWorkList();

    Work work(String id, String workChoice) throws WorkedTooRecentlyException, WorkLogNotExistsException;
}
