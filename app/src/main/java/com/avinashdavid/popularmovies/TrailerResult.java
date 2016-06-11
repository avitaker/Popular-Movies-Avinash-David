package com.avinashdavid.popularmovies;

/**
 * Created by avinashdavid on 5/30/16.
 */
public class TrailerResult {
    public String videoId;
    public String videoKey;
    public String videoSite;
    public String videoName;
    public String videoType;

    public TrailerResult(String videoId) {
        this.videoId = videoId;
    }

    public TrailerResult(String videoId, String videoKey, String videoSite, String videoName, String videoType) {
        this.videoId = videoId;
        this.videoKey = videoKey;
        this.videoSite = videoSite;
        this.videoName = videoName;
        this.videoType = videoType;
    }

    public String getVideoKey() {
        return videoKey;
    }

    public void setVideoKey(String videoKey) {
        this.videoKey = videoKey;
    }

    public String getVideoSite() {
        return videoSite;
    }

    public void setVideoSite(String videoSite) {
        this.videoSite = videoSite;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }
}
