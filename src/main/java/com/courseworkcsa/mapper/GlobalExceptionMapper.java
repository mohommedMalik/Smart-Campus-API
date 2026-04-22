/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.courseworkcsa.mapper;

/**
 *@author Malik
 *Student ID - w2121305
 */
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable>{
    
    @Override
    public Response toResponse(Throwable exception){
        exception.printStackTrace(); //print the error in the terminal
        
        String error = "{\"error\": \"An unexpected internal server error occurred.\"}" ;
        
        return Response.status(500) //internal server error
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
