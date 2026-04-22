/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.courseworkcsa.mapper;

/**
 *@author Malik
 *Student ID - w2121305
 */
import com.courseworkcsa.exceptions.LinkedResourceNotFoundException;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Provider
public class LinkedResourceNotFoundExceptionMapper implements ExceptionMapper<LinkedResourceNotFoundException> {
    @Override
    public Response toResponse(LinkedResourceNotFoundException exception){
        String error = "\"error\": \"" + exception.getMessage() + "\"}" ;
        
        
        return Response.status(422) //unprocessable
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
    
}
