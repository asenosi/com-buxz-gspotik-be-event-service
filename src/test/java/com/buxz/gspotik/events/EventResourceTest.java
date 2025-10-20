package com.buxz.gspotik.events;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
class EventResourceTest {

    @Inject
    EventRepository repository;

    @BeforeEach
    void cleanDatabase() {
        QuarkusTransaction.requiringNew().run(() -> repository.deleteAll());
    }

    @Test
    void shouldCreateAndFetchEvent() {
        Instant start = futureDaysFromNow(2);
        Instant end = start.plus(3, ChronoUnit.HOURS);

        long id =
                given()
                        .contentType(ContentType.JSON)
                        .body(eventPayload("Tech Conference", "Learn about Quarkus", start, end, 150))
                .when()
                        .post("/events")
                .then()
                        .statusCode(201)
                        .body("title", is("Tech Conference"))
                        .extract()
                        .jsonPath()
                        .getLong("id");

        given()
                .when()
                .get("/events/" + id)
                .then()
                .statusCode(200)
                .body("title", is("Tech Conference"))
                .body("description", is("Learn about Quarkus"))
                .body("capacity", is(150));
    }

    @Test
    void shouldListEventsInChronologicalOrder() {
        Instant earlyStart = futureDaysFromNow(3);
        Instant earlyEnd = earlyStart.plus(2, ChronoUnit.HOURS);
        Instant lateStart = futureDaysFromNow(5);
        Instant lateEnd = lateStart.plus(2, ChronoUnit.HOURS);

        given()
                .contentType(ContentType.JSON)
                .body(eventPayload("Morning Workshop", "Hands-on session", earlyStart, earlyEnd, 40))
                .post("/events")
                .then()
                .statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .body(eventPayload("Evening Networking", "Meet peers", lateStart, lateEnd, 60))
                .post("/events")
                .then()
                .statusCode(201);

        given()
                .when()
                .get("/events")
                .then()
                .statusCode(200)
                .body("title", contains("Morning Workshop", "Evening Networking"));
    }

    @Test
    void shouldUpdateAndDeleteEvent() {
        Instant start = futureDaysFromNow(4);
        Instant end = start.plus(4, ChronoUnit.HOURS);

        long id =
                given()
                        .contentType(ContentType.JSON)
                        .body(eventPayload("Product Demo", "Showcase", start, end, 75))
                .when()
                        .post("/events")
                .then()
                        .statusCode(201)
                        .extract()
                        .jsonPath()
                        .getLong("id");

        Instant updatedStart = futureDaysFromNow(6);
        Instant updatedEnd = updatedStart.plus(2, ChronoUnit.HOURS);

        given()
                .contentType(ContentType.JSON)
                .body(eventPayload("Product Demo", "Updated agenda", updatedStart, updatedEnd, 120))
                .when()
                .put("/events/" + id)
                .then()
                .statusCode(200)
                .body("description", is("Updated agenda"))
                .body("capacity", is(120));

        given()
                .when()
                .delete("/events/" + id)
                .then()
                .statusCode(204);

        given()
                .when()
                .get("/events/" + id)
                .then()
                .statusCode(404);
    }

    @Test
    void shouldReturnNotFoundForMissingEvent() {
        given()
                .when()
                .get("/events/9999")
                .then()
                .statusCode(404);
    }

    private Map<String, Object> eventPayload(
            String title,
            String description,
            Instant start,
            Instant end,
            int capacity) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("title", title);
        payload.put("description", description);
        payload.put("startTime", start.toString());
        payload.put("endTime", end.toString());
        payload.put("location", "Conference Hall");
        payload.put("capacity", capacity);
        return payload;
    }

    private Instant futureDaysFromNow(int days) {
        return Instant.now().plus(days, ChronoUnit.DAYS).truncatedTo(ChronoUnit.SECONDS);
    }
}
