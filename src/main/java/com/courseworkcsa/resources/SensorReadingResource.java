/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.courseworkcsa.resources;

/**
 *@author Malik
 *Student ID - w2121305
 */
import com.courseworkcsa.repository.DataStore;
import com.courseworkcsa.exceptions.SensorUnavailableException;
import com.courseworkcsa.exceptions.LinkedResourceNotFoundException;
import com.courseworkcsa.model.Sensor;
import com.courseworkcsa.model.SensorReading;
import com.courseworkcsa.model.SensorStatus;

import java.util.*;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {
    private String sensorId;
    
    public SensorReadingResource(String sensorId){
        this.sensorId = sensorId;
    }
    
    @GET
    public Response getReadings(){
        List<SensorReading> readings = DataStore.readings.getOrDefault(sensorId, new ArrayList<>());
        return Response.ok(readings).build();
    }
    
    @POST
    public Response addReading(SensorReading reading){
        Sensor sensor = DataStore.sensors.get(sensorId);
        
        if(sensor == null){
            throw new LinkedResourceNotFoundException("Specified sensor DOES NOT EXISTS!");  
        }
        
        if (sensor.getStatus() == SensorStatus.MAINTENANCE){
            throw new SensorUnavailableException("CANNOT accept readings (currently sensor is inn maintenance)");
        }

        
        reading.setTimestamp(System.currentTimeMillis());
        reading.setId(UUID.randomUUID().toString());
        
        List<SensorReading> readingHistory = 
                DataStore.readings.computeIfAbsent(sensorId, k -> Collections.synchronizedList(new ArrayList<>())
                );
        readingHistory.add(reading);       
        
        sensor.setCurrentValue(reading.getValue());
        
        return Response.status(Response.Status.CREATED).entity(reading).build();
        
    }
    
    
    
}
