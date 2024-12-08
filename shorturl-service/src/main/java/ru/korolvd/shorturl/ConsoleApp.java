package ru.korolvd.shorturl;

import ru.korolvd.shorturl.model.URL;
import ru.korolvd.shorturl.service.URLService;
import ru.korolvd.shorturl.store.MemUserStore;

import java.util.Scanner;

public class ConsoleApp {
    public static void main(String[] args) {
        URLService urlService = new URLService(new MemUserStore());
        Scanner scanner = new Scanner(System.in);
        String menu = """
                get <shortLink> - перейти по короткой ссылке
                get url <uuid> - получить список ссылок пользователя
                get url <uuid> <shortLink> - получить информацию о короткой ссылке
                post url <link> - создать короткую ссылку и нового пользователя (uuid)
                post url <uuid> <link> - создать короткую ссылку пользователя
                put url <uuid> <shortLink> - редактировать короткую ссылку пользователя
                delete url <uuid> <shortLink> - удалить короткую ссылку пользователя
                delete url <uuid> - удалить все короткую ссылки пользователя
                """;
        System.out.println(menu);
        while (true) {
            String[] input = scanner.nextLine().split(" ");
            System.out.println();

            String shortUrl = "test";
            URL url = urlService.getByShortUrl(shortUrl);
            String origUrl = url.getUrl();
        }
    }
}