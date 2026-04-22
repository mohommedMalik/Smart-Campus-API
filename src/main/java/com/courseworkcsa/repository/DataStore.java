/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.courseworkcsa.repository;

/**
 *@author Malik
 *Student ID - w2121305
 */
import java.util.concurrent.ConcurrentHashMap;
import com.courseworkcsa.model.*;
import java.util.* ;

public class DataStore {
    public static Map<String, Room> rooms = new ConcurrentHashMap<>();
    public static Map<String, Sensor> sensors = new ConcurrentHashMap<>();
    public static Map<String, List<SensorReading>> readings = new ConcurrentHashMap<>();
}
