package com.coinarbritages.coinarbritages.common.i18n;

/**
 * Created by ivofernandes on 28/10/15.
 */
public class UnitManager {

    // Singleton
    private static UnitManager instance = new UnitManager();

    private UnitManager(){}

    public static UnitManager getInstance(){
        return instance;
    }

    // Functions
    public String windSpeed() {
        //TODO metric/imperial

        return "m/s";
    }

    public String precipitation() {
        return "mm"; // Don't need any i18n, openweathermap returns always mm
    }
}
