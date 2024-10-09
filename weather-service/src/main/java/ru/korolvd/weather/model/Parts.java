package ru.korolvd.weather.model;

public class Parts {
    private Part day;
    private Part evening;
    private Part morning;
    private Part night;

    public Part getDay() {
        return day;
    }

    public void setDay(Part day) {
        this.day = day;
    }

    public Part getEvening() {
        return evening;
    }

    public void setEvening(Part evening) {
        this.evening = evening;
    }

    public Part getMorning() {
        return morning;
    }

    public void setMorning(Part morning) {
        this.morning = morning;
    }

    public Part getNight() {
        return night;
    }

    public void setNight(Part night) {
        this.night = night;
    }
}
