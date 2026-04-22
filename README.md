# Smart Campus - Sensor & Room Management API

- ## Overview
The Smart Campus API is a RESTful web service designed to manage rooms and sensors within a university campus. It supports operations such as creating rooms, assigning sensors, recording sensor readings, and retrieving structured data.

- ## API Design
Base URL:
http://localhost:8080/CourseworkCSA/api/v1

###  Main Resources:
<table>
  <tr>
    <th>Resource Name</th>
    <th>Path</th>
    <th>Class</th>
  </tr>
  <tr>
    <td>Rooms</td>
    <td>/api/v1/rooms</td>
    <td>RoomResource</td>
  </tr>
    <tr>
    <td>Sensors</td>
    <td>/api/v1/sensors</td>
    <td>SensorResource</td>
  </tr>
    <tr>
    <td>Readings</td>
    <td>/api/v1/sensors/{sensorId}/readings</td>
    <td>SensorReadingResource</td>
  </tr>
</table>



- ## Setup & Execution
### Prerequisites:
- Java JDK 25
- Apache Tomcat (configured in NetBeans)
- NetBeans IDE

### Steps:
1. Clone the repository:
git clone https://github.com/mohommedMalik/Smart-Campus-API.git

2. Open the project in NetBeans

3. Build the project:
Right-click project → Clean and Build

4. Run the project:
Right-click project → Run

5. Access the API:
http://localhost:8080/CourseworkCSA/api/v1

- ## Sample API Requests

### 1. Get API Discovery
```curl -X GET http://localhost:8080/CourseworkCSA/api/v1```

### 2. Create Room
```
curl -X POST http://localhost:8080/CourseworkCSA/api/v1/rooms \ 
-H "Content-Type: application/json" \
-d '{"id":"LIB-301","name":"Library","capacity":50}'
```
### 3. Get All Rooms
```curl -X GET http://localhost:8080/CourseworkCSA/api/v1/rooms```

### 4. Add Sensor
```
curl -X POST http://localhost:8080/CourseworkCSA/api/v1/sensors \
-H "Content-Type: application/json" \
-d '{"id":"TEMP-001","type":"TEMPERATURE","status":"ACTIVE","currentValue":25,"roomId":"LIB-301"}'
```
### 5. Filter Sensors
```curl -X GET "http://localhost:8080/CourseworkCSA/api/v1/sensors?type=Temperature"```

### 6. Add Sensor Reading
```
curl -X POST http://localhost:8080/CourseworkCSA/api/v1/sensors/TEMP-001/readings \
-H "Content-Type: application/json" \
-d '{"value":27.5}'
```

- ## Error Handling
The API implements structured error handling:
<table>
  <tr>
    <th>Status code</th>
    <th>Meaning</th>
  </tr>
  <tr>
    <td>404</td>
    <td>Resource not found</td>
  </tr>
  <tr>
    <td>409</td>
    <td>Conflict (e.g., deleting room with sensors)</td>
  </tr>
  <tr>
    <td>422</td>
    <td>Invalid linked resource</td>
  </tr>
  <tr>
    <td>403</td>
    <td>Sensor unavailable</td>
  </tr>
  <tr>
    <td>500</td>
    <td>Internal server error</td>
  </tr>
</table>
* All errors return JSON responses.


# Coursework Report
- ## Part 1: Service Architecture & Setup
### 1.	Project & Application Configuration

<p> <b> Question 1.1 </b> : In your report, explain the default lifecycle of a JAX-RS Resource class. Is a new instance instantiated for every incoming request, or does the runtime treat it as a singleton? Elaborate on how this architectural decision impacts the way you manage and synchronize your in-memory data structures (maps/lists) to prevent data loss or race conditions.</p>

**Answer:**
<p>By default, a JAX-RX resource class is request-scoped, it means runtime instantiates a new instance of a resource class for every incoming HTTP request. The runtime doesn’t treat the class as a singleton. It means that instance variables within a resource class are not shared across different requests.</p>
<p>This lifecycle matters because if we used instance fields for the DataStore each request would see a different map. In order to maintain the application status constant throughout all the API calls (for example, storing created rooms and sensors) the in memory data structure s need to be shared globally. For instance, in DataStore.java all three collections (rooms, sensors, readings) are defined as public static.</p>

<p>The project uses a static DataStore with ConcurrentHashMap :</p>

```
public class DataStore {
    public static Map<String, Room> rooms = new ConcurrentHashMap<>();
    public static Map<String, Sensor> sensors = new ConcurrentHashMap<>();
    public static Map<String, List<SensorReading>> readings = new ConcurrentHashMap<>();
}
```
<p>ConcurrentHashMap is thread safe by design. It means, more than one thread can safely perform both read and write operations on the data Structure without data corruption. And also, Collections.synchronizedList is used in both Room.sensorIds and each of the sensor’s reading histories, which ensures that any operation iterating on the list doesn’t interfere with the modifications being made to it. This prevents race conditions and data corruption while maintaining scalability.</p>
</details>

### 2.	The ”Discovery” Endpoint
<p><b>Question 1.2 :</b> Why is the provision of ”Hypermedia” (links and navigation within responses) considered a hallmark of advanced RESTful design (HATEOAS)? How does this approach benefit client developers compared to static documentation?</p>

**Answer**
<p>The provision of Hypermedia(HATEOAS) turns an API from a static interface in to a self describing and discoverable system</p>
<p>In the DiscoveryResource.java class, the root endpoint GET/api/v1 returns a response in JSON format with a map named as primary_resource_collections as follows:</p>

```
{
    "api": "Smart Campus -  Sensor & Room Management API",
    "version": "v1",
    "admin_contact_details": "admin@university.com",
    "primary_resource_collections": {
        "rooms": "/api/v1/rooms",
        "sensors": "/api/v1/sensors"
    }
}
```
- **Benefits over static documentation**
  <p>Clients don’t have to hard code any URLs. Instead, they just start form the discovery endpoint and navigate according to the links provided. In case API evolves and a path changes, any client accessing through the discovery URL will seamlessly adapt without the need for recompiling.</p>

- ## Part 2: Room Management
   ### 1.	RoomResource Implementation.
    <p><b>Question 2.1 : When returning a list of rooms, what are the implications of returning only IDs versus returning the full room objects? Consider network bandwidth and client side processing.</b></p>

 **Answer**
<p>SensorRoomsResource.java has the GET/rooms endpoint that returns entire room object with all the fields included (id, name, capacity, sensorIds). Although this ensures that the client gets a fully detailed answer in a one go, there is the issue of over fetching, which involves sending more data than necessary for the client. The client might only require a room name but will receive capacity and the sensorIds in the payload. Massive JSON payloads negatively impact the performance on mobile networks and increase data transfer costs.</p>
 <p>Returning roomIds will save bandwidth significantly. However, it forces the client to perform the N+1 request pattern. Where it makes one request to get the room list and then another to get each room’s details. According to the lectures the most efficient approach   is to allow the clients to define exactly which attribute they require using query parameters. (or so-called sparse fieldsets).  In this way the server is able to return only those attributes which the client requests. And it prevents both over-fetching an unnecessary request.</p>

### 2.	RoomDeletion & Safety Logic
<p><b>Question 2.2 : </b>Is the DELETE operation idempotent in your implementation? Provide a detailed
justification by describing what happens if a client mistakenly sends the exact same DELETE
request for a room multiple times.
</p>

**Answer**
<p>Yes, the DELETE operation in SensorRoomResourse is idempotent. It means that more than one identical requests which have the same effect on the server state as single request.</p>

-  **Duplicate DELETE request scenario.**
  
  **i. First request (DELETE/rooms/101) :**

  <p>Lets assume the room exists and it is empty. The method checks room.getSensorIds().isEmpty() and       executes DataStore.rooms.remove(roomId).<br>
    
  **Response:** 200 ok <br>
  **Server states:** Room 101 removed</p>

  **ii.	Second request (DELETE/rooms/101) :**
  <p>DataStore.rooms.get(roomId) returns null and the method enters the if(room == null) block and returns a 404 Not Found Error.<br>
    
  **Server states:** Room 101 remains removed. No changes occurred during the request 2.</p>

  <p>Since the resulting resource state (deleted) is identical in both cases the operation is considered idempotent. </p>

 - ## Part 3: Sensor Operations & Linking
### 1.	Sensor Resource & Integrity.
<p><b>Question 3.1 : </b>We explicitly use the @Consumes (MediaType.APPLICATION_JSON) annotation on the POST method. Explain the technical consequences if a client attempts to send data in a different format, such as text/plain or application/xml. How does JAX-RS handle this mismatch?</p>

**Answer**
<p>The @POST method in SensorResource.java is annotated with @Consumes (MediaType.APPLICATION_JSON). (this is added to clarify the theoretical explanation)</p>
 
<p>If a client sends a request that has Content-Type values of text/plain or application/xml instead of application/json, the JAX-RS runtime will reject the request prior to reaching the java method.<br>
Context negotiation occurs in the JAX-RS process. The implementation checks whether there is any method capable of consuming the given Content-Type. Upon failing it automatically responds to the client using HTTP status code 415 (Unsupported media file error). The body of the request is ignored by the server and the method code (addSensor()) itself will never be executed.
</p>


- ### 2.	Filtered Retrieval & Search
<p><b>Question 3.2 : </b>You implemented this filtering using @QueryParam. Contrast this with an alternative design where the type is part of the URL path (e.g., /api/vl/sensors/type/CO2). Why is the query parameter approach generally considered superior for filtering and searching collections?</p>

**Answer**
<p>In SensorResource.java, the filtering functionality is implemented through @QueryParam("type") (for example GET /sensors?type=CO2). An alternative design can place the type directly in the URL path such as /sensors/type/CO2. According to the REST principles the URI path should refer to a resource (i.e. noun), whereas a sensor type is not an independent resource since it is just a filtered list of the existing sensors.</p>
<p>Query parameters are superior for filtering primarily. Because they are combinable and optional in nature. In case the university decides to implement filtering based on sensor types and rooms (for example, /sensors?type=TEMPERATURE&roomId=201), the query parameters combine naturally using the & operator. However, in case of filtering using paths, the combination of several filters results in unclear URL hierarchies (for example: /sensors/type/TEMP/room/201), which do not adhere to the principles of RESTful design and makes the API harder to document, consume and maintain</p>

- ## Part 4: Deep Nesting with Sub- Resources
### 1.	The Sub-Resource Locator Pattern
<p><b>Question 4.1 : Discuss the architectural benefits of the Sub-Resource Locator pattern. How does delegating logic to separate classes help manage complexity in large APIs compared to defining every nested path (e.g., sensors/{id}/readings/{rid}) in one massive controller class.</b></p>

**Answer:**
<p>In this project the SensorResource.java class implements the Sub-Resource Locator pattern via the following method: </p>

```
@Path("/{sensorId}/readings")
public SensorReadingResource getReadingResource(@PathParam("sensorId") String sensorId){
  return new SensorReadingResource(sensorId);
}
```
<p>This method does not have an HTTP verb annotation. It servers as a locator that returns an instanse of Sensor Reading Resourse.</p>

- **Architechtural benefits.**
<p>The Sub-Resource Locator pattern maintains the separation of concerns by ensuring that the SensorResource class is not monolithic “God Object”. The POST and GET methods for managing sensor metadata are kept separated from POST and GET methods for managing readings. All reading specific operations are encapsulated by the SensorReadingResource. It makes the codebase modular and easy to maintain. </p>

<p>Sub-Resource Locator pattern is important for dealing with complexity in case of large APIs. In a complex api system, where each path within an API needs to be defined, it will take lots of lines of code to define every nested route within the same controller class. As a result of using Sub-Resource Locator pattern, code becomes readable and reduces the risk of merge conflicts during development.</p>

- ## Part 5: Advanced Error Handling, Exception Mapping & Logging
### 2.	Dependency Validation (422 Unprocessable Entity)
<p><b>Question 5.2</b>Why is HTTP 422 often considered more semantically accurate than a standard 404 when the issue is a missing reference inside a valid JSON payload?</p>

**Answer**
<p>In this project, in SensorResource.addSensor(), if the roomId which is provided in the JSON payload doesn’t exist, a LinkedResourceNotFoundException is thrown. The Mapper catches this exception and returns HTTP 422 unprocessable entity status.</p>
<p>HTTP 404 Not Found is strictly refers to the Request URI. In this project client sends a request to POST /api/v1/sensors. But the URI does exists. Responding with a 404 will incorrectly imply that the client mistyped the URI.</p>
<p>HTTP 422 Unprocessable entity represents that the server recognized the syntax and the URI provided. But it could not process the sementic instrctions in the entity body. While the json is valid, the foreign key relationship in the json data isn’t fullfilled. Therefore, 422 appropriately address states, “I got the resourse that you requested, although the data that you gave me points to something that does not exist”</p>

### 4.	The Global Safety Net (500).  
<p><b>Question 5.4 : </b>From a cybersecurity standpoint, explain the risks associated with exposing internal Java stack traces to external API consumers. What specific information could an attacker gather from such a trace?</p>

**Answer**
<p>Exposing a stack trace is considered as the one of the most information leakages. Risks of exposing raw java stack traces as follows.</p>
<p>•	A stack trace exposes the exact internal file paths of the server’s file system. This information may help attackers to get an understanding about the structure of the development environment.</p>
<p>•	Stack trace exposes the specific library and framework details. Attackers will be able to find records of the same libraries that are publicly available. If the trace exposes an old version of a library , the attacker will be able to launch a specific attack using the vulnerability.</p>
<p>•	In case an unhandled database error may expose partial SQL queries or any othe important details, aiding in SQL injection attacks.</p>

### 5.	API Request & Response Logging Filters
<p><b>Question 5.5 : </b>Why is it advantageous to use JAX-RS filters for cross-cutting concerns like logging, rather than manually inserting Logger.info() statements inside every single re source method?</p>

**Answer**
<p>Advantages of using filters as follows:</p>
<table>
  <tr>
    <th>Advantage</th>
    <th>Explanation</th>
  </tr>
  <tr>
    <td>Cross-cutting concern</td>
    <td>Logging is a infrastructural concern that should not be considered as a business logic. Adding logger.info() to every resource class violates the DRY principle. The logging logic can be centralized into a one place using a filter.</td>
  </tr>
  <tr>
    <td>Guaranteed execution</td>
    <td>The @Provider filter will intercept the incoming request before any of the resource methods are invoked. It ensures that all calls are logged regardless of the creation of new resourse classes in the future.</td>
  </tr>
  <tr>
    <td>Separation of concerns</td>
    <td>Recourse classes remain focused, testable and clean.there is no presence of debugging messages, adhering to the single responsibility principle.</td>
  </tr>
  
</table>

<p>end of the readme.md - 5COSC022W Client-ServerArchitectures-Coursework(2025/26)</p>
