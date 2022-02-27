package com.example.weather;

public class WeatherRVmodal {
    private String Day;
    private String maxTemp;
    private String minTemp;
    private String cardImage;
    private String condition;

    //setter
    WeatherRVmodal(String Day, String maxTemp, String minTemp, String cardImage, String condition) {
        this.Day = Day;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.cardImage = cardImage;
        this.condition = condition;
    }

    public String getDay() {
        return Day;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public String getCardImage() {
        return cardImage;
    }

    public String getCondition() {
        return condition;
    }
}


/*
public class WeatherRVmodel {
    private String temp;
    private String time;
    private String weaicon;
    private String windspeed;

    public WeatherRVmodel(String temp, String time, String weaicon, String windspeed) {
        this.temp = temp;
        this.time = time;
        this.weaicon = weaicon;
        this.windspeed = windspeed;
    }
    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWeaicon() {
        return weaicon;
    }

    public void setWeaicon(String weaicon) {
        this.weaicon = weaicon;
    }

    public String getWindspeed*/
