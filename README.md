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
