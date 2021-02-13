package com.discordbot.dnd.services;

import com.discordbot.dnd.entities.Work;
import com.discordbot.dnd.entities.WorkLog;

public interface WorkLogService {

    WorkLog getWork(String id);

    WorkLog create(String id, Work work);

    WorkLog update(WorkLog workLog);
}
