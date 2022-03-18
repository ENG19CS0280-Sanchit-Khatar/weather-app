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
