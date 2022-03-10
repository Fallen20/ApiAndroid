package com.example.apiAndroid;

public class EpisodeJson {
    public String episode;
    public String url;

    public EpisodeJson(String episode, String url) {
        this.episode = episode;
        this.url = url;
    }

    public EpisodeJson() {
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
