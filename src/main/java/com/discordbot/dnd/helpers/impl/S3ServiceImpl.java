package com.discordbot.dnd.helpers.impl;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.discordbot.dnd.helpers.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class S3ServiceImpl implements S3Service {
    private static final String BUCKET_NAME = "dnddiscordbot";

    @Autowired
    private AmazonS3Client amazonS3Client;

    @Override
    public S3Object getDocument(String documentName) {
        return amazonS3Client.getObject(BUCKET_NAME, documentName);
    }

    @Override
    public void uploadDocument(String documentName, String content) {
        amazonS3Client.putObject(BUCKET_NAME, documentName, content);
    }
}
