package com.discordbot.dnd.services.impl;

import com.discordbot.dnd.entities.Pirate;
import com.discordbot.dnd.entities.Work;
import com.discordbot.dnd.entities.WorkLog;
import com.discordbot.dnd.exceptions.WorkLogNotExistsException;
import com.discordbot.dnd.exceptions.WorkedTooRecentlyException;
import com.discordbot.dnd.repositories.WorkRepository;
import com.discordbot.dnd.services.InventoryService;
import com.discordbot.dnd.services.PirateService;
import com.discordbot.dnd.services.WorkLogService;
import com.discordbot.dnd.services.WorkService;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class WorkServiceImpl implements WorkService {

    @Autowired
    private WorkRepository workRepository;

    @Autowired
    private WorkLogService workLogService;

    @Autowired
    private PirateService pirateService;

    @Autowired
    private InventoryService inventoryService;

    @Override
    public List<String> getWorkList() {
        return workRepository.getWorkList();
    }

    @Override
    public Work work(String id, String workChoice) throws WorkedTooRecentlyException, WorkLogNotExistsException {
        if(Strings.isEmpty(workChoice)) {
            WorkLog workLog = workLogService.getWork(id);
            if(workLog == null) {
                throw new WorkLogNotExistsException("Work log does not exist for id" + id);
            }
            if (DateUtils.addMinutes(workLog.getDate(), workLog.getWork().getTimeout()).before(new Date())) {
                workLog.setDate(new Date());
                long currentGold = workLog.getPirate().getInventory().getGold();
                currentGold = currentGold + workLog.getWork().getPayout();
                workLog.getPirate().getInventory().setGold(currentGold);
                inventoryService.update(workLog.getPirate().getInventory());
                pirateService.update(workLog.getPirate());
                workLogService.update(workLog);
                return workLog.getWork();
            } else {
                Duration duration = Duration.between(new Date().toInstant(), DateUtils.addMinutes(workLog.getDate(), workLog.getWork().getTimeout()).toInstant());
                throw new WorkedTooRecentlyException("Pirate " + id + " has worked too recently", (int) duration.toMinutes());
            }
        } else {
            Work work = workRepository.getWorkByWorkType(workChoice);
            WorkLog workLog = workLogService.getWork(id);
            if(workLog == null) {
                workLogService.create(id, work);
                return work;
            } else if (DateUtils.addMinutes(workLog.getDate(), workLog.getWork().getTimeout()).before(new Date())) {
                workLog.setWork(work);
                workLog.setDate(new Date());
                long currentGold = workLog.getPirate().getInventory().getGold();
                currentGold = currentGold + workLog.getWork().getPayout();
                workLog.getPirate().getInventory().setGold(currentGold);
                inventoryService.update(workLog.getPirate().getInventory());
                pirateService.update(workLog.getPirate());
                workLogService.update(workLog);
                return work;
            } else {
                Duration duration = Duration.between(new Date().toInstant(), DateUtils.addMinutes(workLog.getDate(), workLog.getWork().getTimeout()).toInstant());
                throw new WorkedTooRecentlyException("Pirate " + id + " has worked too recently", (int) duration.toMinutes());
            }
        }
    }
}
