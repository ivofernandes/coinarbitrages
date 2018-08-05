package com.coinarbritages.coinarbritages.common.configuration;

import android.graphics.Color;

/**
 * Created by Ivo on 26-06-2015.
 */
public class LayoutManager {

    // Singleton
    private static LayoutManager instance = new LayoutManager();

    private LayoutManager(){}

    public static LayoutManager getInstance(){
        return instance;
    }

    // Getters for colors
    public int getBackgroundColor(){
        return Color.parseColor("#000000");
    }

    public int getSmoothBackgroundColor(){
        return Color.parseColor("#000000");
    }

    public int getSmoothForegroundColor(){
        return Color.parseColor("#a8a8a8");
    }

    public int getForegroundColor(){
        return Color.parseColor("#f9f9f9");
    }

}
