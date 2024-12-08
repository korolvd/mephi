package ru.korolvd.shorturl.model;

import java.util.List;
import java.util.UUID;

public class User {
    private String uuid = UUID.randomUUID().toString();

    private List<URL> urls;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<URL> getUrls() {
        return urls;
    }

    public void setUrls(List<URL> urls) {
        this.urls = urls;
    }

    public void addUrl(URL url) {
        url.setUserId(this.uuid);
        urls.add(url);
    }
}
