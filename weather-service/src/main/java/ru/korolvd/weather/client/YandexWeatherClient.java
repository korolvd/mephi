package ru.korolvd.weather.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;

public class YandexWeatherClient implements WeatherClient {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String apiUrl;
    private final String weatherKey;

    public YandexWeatherClient() {
        ResourceBundle config = ResourceBundle.getBundle("app");
        this.apiUrl = config.getString("yandex.weather.url");
        this.weatherKey = config.getString("yandex.weather.key");
    }

    @Override
    public String getInfo(double lat, double lon, int limit) {
        String result = null;
        String url = apiUrl + "/forecast?lat=" + lat + "&lon=" + lon + "&limit=" + limit;
        try {
            HttpRequest request = HttpRequest.newBuilder(new URI(url))
                    .header("X-Yandex-Weather-Key", weatherKey)
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 300) {
                result = response.body();
            } else {
                throw new Exception("Response code " + response.statusCode() + ": " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
