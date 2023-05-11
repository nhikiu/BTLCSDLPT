/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.btlcsdlpt;

import java.sql.Time;

public class TrainRide {
    private String id;
    private String DepatureStation;
    private Time DepatureTime;

    public TrainRide() {
    }
    
    

    public TrainRide(String id, String DepatureStation, Time DepatureTime) {
        this.id = id;
        this.DepatureStation = DepatureStation;
        this.DepatureTime = DepatureTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepatureStation() {
        return DepatureStation;
    }

    public void setDepatureStation(String DepatureStation) {
        this.DepatureStation = DepatureStation;
    }

    public Time getDepatureTime() {
        return DepatureTime;
    }

    public void setDepatureTime(Time DepatureTime) {
        this.DepatureTime = DepatureTime;
    }

    @Override
    public String toString() {
        return "TrainRide{" + "id=" + id + ", DepatureStation=" + DepatureStation + ", DepatureTime=" + DepatureTime + '}';
    }
    
    
}
