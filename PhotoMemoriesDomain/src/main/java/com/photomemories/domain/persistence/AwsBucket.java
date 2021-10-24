package com.photomemories.domain.persistence;

public enum AwsBucket {
    PROFILE_IMAGE("photo-memories-bucket");

    private final String awsBucket;

    AwsBucket(String awsBucket) {
        this.awsBucket = awsBucket;
    }

    public String getAwsBucket() {
        return awsBucket;
    }
}
