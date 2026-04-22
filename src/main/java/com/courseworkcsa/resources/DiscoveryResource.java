/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.courseworkcsa.resources;

/**
 *@author Malik
 *Student ID - w2121305
 */

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Map;

@Path("/")
public class DiscoveryResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getApiInfo(){
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("api", "Smart Campus -  Sensor & Room Management API");
        response.put("version", "v1");
        response.put("admin_contact_details", "admin@university.com");
        
        Map<String, String> resourceCollection = new HashMap<>();
        resourceCollection.put("rooms", "/api/v1/rooms");
        resourceCollection.put("sensors", "/api/v1/sensors");
        
        response.put("primary_resource_collections", resourceCollection);
        
        return Response.ok(response).build();
    }
}
