package com.mycompany.btlcsdlpt;

import java.util.ArrayList;

public class Query {
    private ArrayList<TrainRide> list;
    private String message;
    private boolean isSuccess;

    public Query(ArrayList<TrainRide> list, String message,boolean isSuccess) {
        this.list = list;
        this.message = message;
        this.isSuccess = isSuccess;
    }

    public ArrayList<TrainRide> getList() {
        return list;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public boolean Success(){
        return isSuccess;
    }  
}
