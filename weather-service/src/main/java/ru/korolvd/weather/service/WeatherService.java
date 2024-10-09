package ru.korolvd.weather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import ru.korolvd.weather.client.WeatherClient;
import ru.korolvd.weather.model.Forecast;
import ru.korolvd.weather.model.ForecastInfo;

import java.util.List;

import static java.util.Objects.*;

public class WeatherService {
    private final int MAX_LIMIT;
    private final WeatherClient client;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    ;

    public WeatherService(WeatherClient client, int maxLimit) {
        this.client = requireNonNull(client);
        this.MAX_LIMIT = maxLimit;
    }

    public String getRawForecast(double lat, double lon, int limit) {
        if (limit < 1 || limit > MAX_LIMIT) {
            throw new IllegalArgumentException("Incorrect limit (" + limit + "). Required in 0 ... " + MAX_LIMIT);
        }
        return client.getInfo(lat, lon, limit);
    }

    public ForecastInfo getForecast(double lat, double lon, int limit) throws JsonProcessingException {
        String raw = getRawForecast(lat, lon, limit);
        return nonNull(raw) ? parseForecastInfo(raw) : null;
    }

    public int calculateAvgTemp(List<Forecast> forecasts) {
        int avg = 0;
        for (Forecast forecast : forecasts) {
            int tempDay = forecast.getParts().getNight().getTempAvg();
            tempDay += forecast.getParts().getMorning().getTempAvg();
            tempDay += forecast.getParts().getDay().getTempAvg();
            tempDay += forecast.getParts().getEvening().getTempAvg();
            avg += tempDay / 4;
        }
        return avg / forecasts.size();
    }

    public ForecastInfo parseForecastInfo(String rawForecastInfo) throws JsonProcessingException {
        return objectMapper.readValue(rawForecastInfo, ForecastInfo.class);
    }
}
