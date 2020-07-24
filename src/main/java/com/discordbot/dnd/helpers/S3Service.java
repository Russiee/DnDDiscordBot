package com.discordbot.dnd.helpers;

import com.amazonaws.services.s3.model.S3Object;

public interface S3Service {

    S3Object getDocument(String documentName);

    void uploadDocument(String documentName, String content);
}
