package ru.korolvd.shorturl.util;

import java.util.UUID;

public class URLUtil {

    public static String generateShortBy(String url) {
        return UUID.randomUUID().toString().substring(0, 10); //TODO
    }
}
