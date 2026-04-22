/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.courseworkcsa.filter;

/**
 *@author Malik
 *Student ID - w2121305
 */
import java.io.IOException;
import java.util.logging.Logger;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {
    
    private static final Logger logger = Logger.getLogger(LoggingFilter.class.getName());
    
    //incomming requests
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException{
        String method = requestContext.getMethod();
        String path = requestContext.getUriInfo().getAbsolutePath().toString();
        
        logger.info("INCOMING REQUEST: Method=" + method + ", URI=" + path);
    }
    
    //outgoing responses
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException{
        int status = responseContext.getStatus();
        
        logger.info("OUTGOING RESPONSE: Status Code=" + status);
    }
    
}
