/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.courseworkcsa.mapper;

/**
 *@author Malik
 *Student ID - w2121305
 */

import com.courseworkcsa.exceptions.RoomNotEmptyException;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Provider
public class RoomNotEmptyMapper implements ExceptionMapper<RoomNotEmptyException>{
    
    @Override
    public Response toResponse(RoomNotEmptyException exception){
        String error = "\"error\": \"" + exception.getMessage() + "\"}" ;
        
        
        return Response.status(409) //conflict
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
