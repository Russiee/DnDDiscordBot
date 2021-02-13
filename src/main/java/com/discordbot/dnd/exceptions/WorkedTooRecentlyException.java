package com.discordbot.dnd.exceptions;

public class WorkedTooRecentlyException extends Exception {
    private final int timeLeft;
    public WorkedTooRecentlyException(String errorMessage, int timeLeft) {
        super(errorMessage);
        this.timeLeft = timeLeft;
    }

    public int getTimeLeft() {
        return timeLeft;
    }
}
