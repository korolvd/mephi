package ru.korolvd.shorturl.service;

import ru.korolvd.shorturl.model.URL;
import ru.korolvd.shorturl.model.User;
import ru.korolvd.shorturl.store.UserStore;

import java.util.List;

import static ru.korolvd.shorturl.util.URLUtil.generateShortBy;

public class URLService {
    private final UserStore userStore;

    public URLService(UserStore userStore) {
        this.userStore = userStore;
    }

    public URL createURL(String url, int limit) {
        String shortUrl = generateShortUrl(url);
        URL newUrl = new URL(url, shortUrl, limit);
        User user = new User();
        user.addUrl(newUrl);
        userStore.add(user);
        return newUrl;
    }

    public URL getByShortUrl(String shortUrl) {
        return userStore.getAll().stream()
                .flatMap(u -> u.getUrls().stream())
                .filter(l -> l.getShortUrl().equals(shortUrl))
                .findFirst()
                .orElse(null); //fixme
    }

    private String generateShortUrl(String url) {
        List<String> shortUrls = userStore.getAll().stream()
                .flatMap(u -> u.getUrls().stream())
                .map(URL::getShortUrl)
                .toList();
        String shortUrl = generateShortBy(url);
        while (shortUrls.contains(shortUrl)) {
            shortUrl = generateShortBy(url);
        }
        return shortUrl;
    }
}
