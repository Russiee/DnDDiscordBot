package com.discordbot.dnd.repositories;

import com.discordbot.dnd.entities.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkRepository extends JpaRepository<Work, String> {

    @Query(nativeQuery = true,
    value = "SELECT work_type FROM work")
    List<String> getWorkList();

    @Query(nativeQuery = true,
    value = "SELECT * FROM work WHERE work_type = :workType")
    Work getWorkByWorkType(String workType);
}
