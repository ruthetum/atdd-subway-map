package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class LineSteps {

    public static CreateLineRequest 지하철_노선_생성_요청(
            String name,
            String color,
            Long upStationId,
            Long downStationId,
            int distance
    ) {
        return new CreateLineRequest(name, color, upStationId, downStationId, distance);
    }

    public static ExtractableResponse<Response> 지하철_노선_생성(CreateLineRequest createLineRequest) {
        return RestAssured
                .given().log().all()
                    .body(createLineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post("/lines")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회() {
        return RestAssured
                .given().log().all()
                .when()
                    .get("/lines")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회(Long id) {
        return RestAssured
                .given().log().all()
                .when()
                    .get("/lines/{id}", id)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정(Long id, String name, String color) {
        return RestAssured
                .given().log().all()
                    .body(new UpdateLineRequest(name, color))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .put("/lines/{id}", id)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제(Long id) {
        return RestAssured
                .given().log().all()
                .when()
                    .delete("/lines/{id}", id)
                .then().log().all().extract();
    }
}