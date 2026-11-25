package de.hsos.swa.mannschaftssport.boundary.rest;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class TeamPlayerFlowTest {

    @Test
    void playerOhneSpielerIdKannNichtInsTeam() {
        int teamId = given().contentType(ContentType.JSON)
                .body("{\"name\":\"FC Test\",\"kategorie\":\"senioren\"}")
                .post("/teams").then().statusCode(201).extract().path("id");
        int playerId = given().contentType(ContentType.JSON)
                .body("{\"vorname\":\"Max\",\"nachname\":\"Muster\",\"position\":\"Sturm\"}")
                .post("/players").then().statusCode(201).extract().path("id");

        given().put("/teams/" + teamId + "/players/" + playerId)
                .then().statusCode(409);

        given().post("/players/" + playerId + "/spielerpass").then().statusCode(200);

        given().put("/teams/" + teamId + "/players/" + playerId)
                .then().statusCode(200);
    }
}
