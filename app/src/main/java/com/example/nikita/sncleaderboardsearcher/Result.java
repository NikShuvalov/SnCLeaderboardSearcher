package com.example.nikita.sncleaderboardsearcher;

import java.util.ArrayList;

/**
 * Created by NikitaShuvalov on 1/17/17.
 */

public class Result {
    String mPosition, mName, mPoints, mCurrentCash, mProjectedCash;

    public Result(String position, String name, String points, String currentCash, String projectedCash) {
        mPosition = position;
        mName = name;
        mPoints = points;
        mCurrentCash = currentCash;
        mProjectedCash = projectedCash;
    }

    public Result (ArrayList<String> values){
        mPosition = values.get(0)+"th";
        mName = values.get(1);
        mPoints = values.get(2);
        mCurrentCash = values.get(3);
        mProjectedCash = values.get(4);
    }

    public String getPosition() {
        return mPosition;
    }

    public String getName() {
        return mName;
    }

    public String getPoints() {
        return mPoints;
    }

    public String getCurrentCash() {
        return mCurrentCash;
    }

    public String getProjectedCash() {
        return mProjectedCash;
    }
}
