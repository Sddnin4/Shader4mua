package me.bonmua;

public class State {

    private Season currentSeason;
    private int currentDay;

    public State() {
        this.currentSeason = Season.XUAN;
        this.currentDay = 1;
    }

    public Season getCurrentSeason() {
        return currentSeason;
    }

    public void setCurrentSeason(Season season) {
        this.currentSeason = season;
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(int day) {
        this.currentDay = day;
    }

    public void incrementDay() {
        currentDay++;
        if (currentDay > 90) {
            currentDay = 1;
            currentSeason = currentSeason.next();
        }
    }
}
