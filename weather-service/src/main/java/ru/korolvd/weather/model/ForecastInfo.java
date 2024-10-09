package ru.korolvd.weather.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class ForecastInfo {
    private Timestamp now;
    private Date nowDt;
    private Fact fact;
    private List<Forecast> forecasts;

    public Timestamp getNow() {
        return now;
    }

    public void setNow(Timestamp now) {
        this.now = now;
    }

    public Date getNowDt() {
        return nowDt;
    }

    public void setNowDt(Date nowDt) {
        this.nowDt = nowDt;
    }

    public Fact getFact() {
        return fact;
    }

    public void setFact(Fact fact) {
        this.fact = fact;
    }

    public List<Forecast> getForecasts() {
        return forecasts;
    }

    public void setForecasts(List<Forecast> forecasts) {
        this.forecasts = forecasts;
    }
}
