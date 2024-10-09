package ru.korolvd.weather.model;

import java.util.Date;

public class Forecast {
    private Date date;
    private Parts parts;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Parts getParts() {
        return parts;
    }

    public void setParts(Parts parts) {
        this.parts = parts;
    }
}
