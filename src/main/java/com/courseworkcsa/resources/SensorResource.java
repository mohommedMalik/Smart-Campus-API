/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.courseworkcsa.resources;

/**
 *@author Malik
 *Student ID - w2121305
 */
import com.courseworkcsa.model.Sensor;
import com.courseworkcsa.model.SensorType;
import com.courseworkcsa.repository.DataStore;
import com.courseworkcsa.exceptions.LinkedResourceNotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import java.util.*;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {
    
    @POST
    public Response addSensor(Sensor sensor){
        if (!DataStore.rooms.containsKey(sensor.getRoomId())){
            throw new LinkedResourceNotFoundException("Specified roomId DOES NOT EXISTS!");
        }
        DataStore.sensors.put(sensor.getId(), sensor);
        DataStore.rooms.get(sensor.getRoomId()).getSensorIds().add(sensor.getId());
        
        return Response.status(Response.Status.CREATED).entity(sensor).build();
    }
    
    @GET
    public Response getSensors(@QueryParam("type") SensorType type){
        List<Sensor> sensorList = new ArrayList<>(DataStore.sensors.values());
        
        if(type == null){
            return Response.ok(sensorList).build();
        }
        
        List<Sensor> sensorsFiltered = new ArrayList<>();
        for(Sensor sensor : sensorList){
            if(sensor.getType() == type){
                sensorsFiltered.add(sensor);
            }       
        }
        return Response.ok(sensorsFiltered).build();
    }
    
    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadingResource(@PathParam("sensorId") String sensorId){
        return new SensorReadingResource(sensorId);
    }
    
    
}
