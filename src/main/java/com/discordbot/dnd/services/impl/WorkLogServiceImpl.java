package com.discordbot.dnd.services.impl;

import com.discordbot.dnd.entities.Pirate;
import com.discordbot.dnd.entities.Work;
import com.discordbot.dnd.entities.WorkLog;
import com.discordbot.dnd.repositories.WorkLogRepository;
import com.discordbot.dnd.services.PirateService;
import com.discordbot.dnd.services.WorkLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class WorkLogServiceImpl implements WorkLogService {
    @Autowired
    private WorkLogRepository workLogRepository;
    @Autowired
    private PirateService pirateService;

    @Override
    public WorkLog getWork(String id) {
        return workLogRepository.getWork(id);
    }

    @Override
    public WorkLog create(String id, Work work) {
        Pirate pirate = pirateService.getPirate(id);
        WorkLog workLog = new WorkLog();
        workLog.setId(UUID.randomUUID().toString());
        workLog.setWork(work);
        workLog.setPirate(pirate);
        workLog.setDate(new Date());
        return workLogRepository.saveAndFlush(workLog);
    }

    @Override
    public WorkLog update(WorkLog workLog) {
        return workLogRepository.saveAndFlush(workLog);
    }
}
