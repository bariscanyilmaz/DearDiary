package com.bariscanyilmaz.deardiary.model;

public class MoodItem {

    public final String text;
    public final int icon;

    public MoodItem(String text, Integer icon) {
            this.text = text;
            this.icon = icon;
    }

    @Override
    public String toString() {
        return text;
    }

}
