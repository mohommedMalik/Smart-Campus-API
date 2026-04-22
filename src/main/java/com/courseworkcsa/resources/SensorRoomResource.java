/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.courseworkcsa.resources;

/**
 *@author Malik
 *Student ID - w2121305
 */
import com.courseworkcsa.model.Room;
import com.courseworkcsa.repository.DataStore;
import com.courseworkcsa.exceptions.RoomNotEmptyException;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorRoomResource {
    
   @GET
   public List<Room> getAllRooms(){
       return new ArrayList<>(DataStore.rooms.values());
   }
   
   @POST
   public Response createRoom(Room room){
       DataStore.rooms.put(room.getId(), room);
       return Response.status(Response.Status.CREATED).entity(room).build();
   }
   
   @GET
   @Path("/{roomId}")
   public Response getRoomById(@PathParam("roomId") String roomId){
       Room room = DataStore.rooms.get(roomId);
       
       if(room == null){
           return Response.status(Response.Status.NOT_FOUND).entity("Error: Room not found!.").build();
       }
       
       return Response.ok(room).build();
       
   }
   
   @DELETE
   @Path("/{roomId}")
   public Response deleteRoom(@PathParam("roomId") String roomId){
       Room room = DataStore.rooms.get(roomId);
       
        if(room == null){
           return Response.status(Response.Status.NOT_FOUND).entity("Error: Room not found!.").build();
        }
       
        if (!room.getSensorIds().isEmpty()) {
            // Throw new custom exception
            throw new RoomNotEmptyException(
                "Cannot delete. Room currently has active hardware."
            );
        }
        
        DataStore.rooms.remove(roomId);
        
        return Response.ok("Room deleted successfully!").build();
   }
   
    
}
