package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class LineSteps {

    public static LineRequest 지하철_노선_생성_요청(
            String name,
            String color,
            Long upStationId,
            Long downStationId,
            int distance
    ) {
        return new LineRequest(name, color, upStationId, downStationId, distance);
    }

    public static ExtractableResponse<Response> 지하철_노선_생성(LineRequest lineRequest) {
        return RestAssured
                .given().log().all()
                    .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post("/lines")
                .then().log().all().extract();
    }
}
