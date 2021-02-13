package com.discordbot.dnd.repositories;

import com.discordbot.dnd.entities.WorkLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkLogRepository extends JpaRepository<WorkLog, String> {

    @Query(nativeQuery = true,
    value = "SELECT * FROM work_log WHERE pirate_id = :id")
    WorkLog getWork(String id);
}
