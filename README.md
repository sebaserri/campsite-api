# campsite-api

A REST API for reservation using Java, Spring Boot, Spring Data MongoDB and Mockito.

## Install

```bash
mvn clean install
```

## Run

```bash
mvn spring-boot:run
```

Runs on port 8080: http://localhost:8080

## Endpoints

### Fetch all reservations
Method: GET - URL: /api/book/

### Creates a reservation and return a bookingId 
Method: POST - URL: /api/book/ - Payload: 
```bash
{
	"name": "Sebastian",
	"lastname": "Serri",
	"email": "sebastian@gmail.com",
	"dateFrom": "2019-02-10",
	"dateTo": "2019-02-13"
}
```

### Fetch a reservation returning all reservation data
Method: GET - URL: /api/book/f54580e3-0fd4-4923-bc06-e9383257533b

### Updates a reservation and return bookingId
Method: PUT - URL: /api/book/ - Payload:

```bash
{
    "bookingId": "f54580e3-0fd4-4923-bc06-e9383257533b",
	"name": "Sebastian",
	"lastname": "Serri",
	"email": "sebastian@gmail.com",
	"dateFrom": "2019-02-10",
	"dateTo": "2019-02-13"
}
```

### Notifies if the Campsite is available. If no date define return a month
Method: GET - URL: /api/book/available?dateFrom=2019-02-10&dateTo=2019-02-13
Method: GET - URL: /api/book/available

### Deletes a reservation and return the bookingId erased
Method: DELETE - URL: /api/book - Payload:

```bash
{
    "bookingId": "f54580e3-0fd4-4923-bc06-e9383257533b"
}
```

Import file "Campsite API.postman_collection.json" to run the test on Postman. There are endpoints for reservations.


## Test

```bash
mvn test
```
