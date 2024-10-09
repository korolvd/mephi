package ru.korolvd.weather.client;

public interface WeatherClient {

    String getInfo(double lat, double lon, int limit);

}
