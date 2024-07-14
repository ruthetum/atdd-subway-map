package subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class StationSteps {

    public static StationRequest 지하철역_생성_요청(String name) {
        return new StationRequest(name);
    }

    public static ExtractableResponse<Response> 지하철역_생성(StationRequest stationRequest) {
        return RestAssured
                .given().log().all()
                    .body(stationRequest)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post("/stations")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철역_조회() {
        return RestAssured
                .given().log().all()
                .when()
                    .get("/stations")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철역_제거(Long id) {
        return RestAssured
                .given().log().all()
                    .when()
                        .delete("/stations/{id}", id)
                    .then().log().all().extract();
    }
}
