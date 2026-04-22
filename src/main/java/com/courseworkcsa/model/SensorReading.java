/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.courseworkcsa.model;

/**
 *@author Malik
 *Student ID - w2121305
 */
public class SensorReading {
    private String id;
    private long timestamp;
    private double value; 

    //constructors
    public SensorReading() {
    }
    public SensorReading(String id, long timestamp, double value) {
        this.id = id;
        this.timestamp = timestamp;
        this.value = value;
    }
    
    //getters and setters

    public String getId() { return id;}
    public void setId(String id) { this.id = id;}

    public long getTimestamp() { return timestamp;}
    public void setTimestamp(long timestamp) { this.timestamp = timestamp;}

    public double getValue() { return value;}
    public void setValue(double value) { this.value = value;}
       
     
}
