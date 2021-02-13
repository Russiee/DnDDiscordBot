package com.discordbot.dnd.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "work")
public class Work {

    @Id
    private String id;

    @Column(name = "work_type")
    private String workType;

    private long payout;

    @OneToMany(cascade=CascadeType.PERSIST, fetch=FetchType.EAGER)
    private List<WorkLog> worklog;

    private int timeout;

    public Work(String id, String workType, long payout) {
        this.id = id;
        this.workType = workType;
        this.payout = payout;
    }

    public Work() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public long getPayout() {
        return payout;
    }

    public void setPayout(long payout) {
        this.payout = payout;
    }

    public List<WorkLog> getWorklog() {
        return worklog;
    }

    public void setWorklog(List<WorkLog> worklog) {
        this.worklog = worklog;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
