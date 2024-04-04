# README

## System Design

### Tech stack
* Version: Java 17
* Framework: Spring, JPA, Hibernate
* Database: H2 (in memory)

### Dev tools
* Intellij
* Postman
* Terminal

### Database Schema
Tables

1. **trains**: This table contains list of trains, with train_number & train name
2. **routes**: This is table contains list of `from` and `to` stations and the `price` for the journey. Also it will contain the seats available for the route. This is expected to be pre-populated by another service. The main reason behind this table is to enable the **Searching** later on. However, it was not the requirement currently.
3. **users**: This table contains the list of registered users who will book the ticket via this software platform.
4. **tickets**: This table contains the booking status for a given user. The table contains columns such as "passenger_name", "passenger_age" etc. This is to allow that for a user to book ticket for a different passenger, such as friends and family.

Migrations are handled using liquibase.
Currently, the trains table and routes table are pre-populated with using liquibase changelogs so that the functionality can be demonstrated.

### Current Functionality
1. User is automatically onboarded into the platform during when they are first booking the ticket.
2. Currently, the `passenger_name` is derived from the username itself, and other parameters such as passenger_age, passenger_sex are not accepted in the API for now.
3. Ticket reservation, ticket listing for a given train & section, updating the seat of a ticket are supported.
4. Deleting a user won't cause their reserved ticket deletion. It will set the foreign key id in the tickets for user to null. This needs to further thought through for exactly how it should behave for completed and non-completed journeys.

### Limitations

1. The route update and ticket allocation is currently not considering any date range based allocation. This is a major limitation, the system is unable to book the same seat for future dates.
2. The ticket booking flow need to be enhanced such that race condition during booking is prevented.
3. OAuth2 + JWT based authentication should be supported so that the APIs are secured.
4. API response can be enhanced with java beans instead of json node.
5. API documentation using swagger can be done and packaging can be improved

## Demo

For this demo, I will use the complete terminal based commands so that it is easier to reproduce in other laptop without any additional software.
However, for the developer productivity one should consider using IDE such as Intellij, and API test client such as Postman which allow debugging & saving the workflows.

## How to run the application

Terminal
```bash
mvn clean install
java -jar target/trainticket-0.0.1-SNAPSHOT.jar

# also can be run with `mvn spring-boot:run` for dev purposes
```

### Reserve a ticket
```bash
curl --location 'localhost:8080/book-ticket' \
--header 'Content-Type: application/json' \
--data-raw '{
    "fromStation": "London",
    "toStation": "France",
    "pricePaid": 20,
    "user": {
        "firstName": "Guruprasad",
        "lastName": "Bhat",
        "email": "bpg168@gmail.com"
    }
}' -v
```
![02_reserve_ticket.png](assets%2F02_reserve_ticket.png)

### Retrieve booked Ticket
Note: please chagne the pnr number to the one returned in the previous step
```bash
curl --location 'localhost:8080/ticket/5554285928' -v
```
![03_retrieve_existing_ticket_by_pnr.png](assets%2F03_retrieve_existing_ticket_by_pnr.png)

### Retrieve list of tickets for a given train & section
Before this I have reserved one more ticket for demo purpose
```bash
curl --location 'localhost:8080/ticket/1001/A' | jq
```
![04_retrieve_ticket_for_train_1001_section_A.png](assets%2F04_retrieve_ticket_for_train_1001_section_A.png)

### Delete a User
Deleting a user won't cause the tickets to be deleted. Current deletion is supported either by email_id or by user_id.
Below demonstration with email id.
```bash
# notice user exists before
curl --location --request GET 'localhost:8080/user?email=bpg168%40gmail.com'

# delete the user
curl --location --request DELETE 'localhost:8080/user?email=bpg168%40gmail.com'

# notice the user is not there anymore
curl --location --request GET 'localhost:8080/user?email=bpg168%40gmail.com'

# notice their ticket reserved in the past still exists but foreign key is made null
curl --location 'localhost:8080/ticket/5554285928' -v
```
![05_deletion_of_user.png](assets%2F05_deletion_of_user.png)


### Change the Seat for a ticket
Changing the seat is done by using PNR to update the ticket. In case of invalid (seat or section) or already reserved seat the api will respond accordingly without updating the seat.
```bash
# retrieve existing ticket notice old seat number
curl --location 'localhost:8080/ticket/5554318388' | jq

# update the seat to new seat & section
curl --location --request PATCH 'localhost:8080/ticket/5554318388' \
--header 'Content-Type: application/json' \
--data '{
    "newSection": "B",
    "newSeat": 10
}' | jq
```
