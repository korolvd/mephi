package ru.korolvd.weather;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.korolvd.weather.client.YandexWeatherClient;
import ru.korolvd.weather.model.ForecastInfo;
import ru.korolvd.weather.service.WeatherService;

import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        Scanner scanner = new Scanner(System.in).useLocale(Locale.US);
        System.out.print("Enter latitude: ");
        double lat = scanner.nextDouble();
        System.out.print("Enter longitude: ");
        double lon = scanner.nextDouble();
        System.out.print("Enter count days: ");
        int limit = scanner.nextInt();

        WeatherService service = new WeatherService(new YandexWeatherClient(), 7);
        String rawForecast = service.getRawForecast(lat, lon, limit);
        System.out.println("Response: " + rawForecast);
        ForecastInfo forecastInfo = service.parseForecastInfo(rawForecast);
        System.out.println("Current temp: " + forecastInfo.getFact().getTemp());
        int avgTemp = service.calculateAvgTemp(forecastInfo.getForecasts());
        System.out.println("Forecasted average temp for " + limit + " days: " + avgTemp);
    }
}
