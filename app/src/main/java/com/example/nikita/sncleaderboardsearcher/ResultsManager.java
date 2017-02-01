package com.example.nikita.sncleaderboardsearcher;

import java.util.ArrayList;

/**
 * Created by NikitaShuvalov on 1/30/17.
 */

public class ResultsManager {
    private ArrayList<Result> mResultsList;
    private String mSearchName;
    private int mFoundPosition;

    public ResultsManager() {
        mResultsList = new ArrayList<>();
    }

    private static ResultsManager sResultsManager;

    public static ResultsManager getInstance() {
        if(sResultsManager== null){
            sResultsManager = new ResultsManager();
        }
        return sResultsManager;
    }

    public ArrayList<Result> getResultsList() {
        return mResultsList;
    }

    public String getSearchName() {
        return mSearchName;
    }

    public void setResultsList(ArrayList<Result> resultsList) {
        mResultsList = resultsList;
    }

    public void setSearchName(String searchName) {
        mSearchName = searchName;
    }

    public void addResult(Result result){
        mResultsList.add(result);
    }

    public int getFoundPosition() {
        return mFoundPosition;
    }

    public void setFoundPosition(int foundPosition) {
        mFoundPosition = foundPosition;
    }
    public void clearResults(){
        mResultsList.clear();
    }
}
