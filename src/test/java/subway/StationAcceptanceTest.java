package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.StationSteps.*;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends ApiTest {
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다")
    @Test
    void createStation() {
        // when
        String stationName = "강남역";
        StationRequest request = 지하철역_생성_요청(stationName);
        ExtractableResponse<Response> createStationResponse = 지하철역_생성(request);

        // then
        assertThat(createStationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        ExtractableResponse<Response> showStationsResponse = 지하철역_조회();
        List<String> stationNames = showStationsResponse.jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf(stationName);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다")
    @Test
    void showStations() {
        // given: 2개의 지하철역 생성
        String stationName1 = "강남역";
        지하철역_생성(지하철역_생성_요청(stationName1));

        String stationName2 = "역삼역";
        지하철역_생성(지하철역_생성_요청(stationName2));

        // when: 지하철역 목록 조회
        ExtractableResponse<Response> response = 지하철역_조회();

        // then: 생성되어 있는 2개의 지하철역 응답
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> stationNames = response.jsonPath().getList("name", String.class);
        assertThat(stationNames).contains(stationName1, stationName2);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 제거하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 제거한다")
    @Test
    void deleteStation() {
        // given: 지하철역 생성
        String stationName = "강남역";
        ExtractableResponse<Response> createStationResponse = 지하철역_생성(지하철역_생성_요청(stationName));
        Long createdStationId = createStationResponse.jsonPath().getLong("id");

        // when: 지하철역 제거
        ExtractableResponse<Response>  deleteStationResponse = 지하철역_제거(createdStationId);

        // then: 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
        assertThat(deleteStationResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        ExtractableResponse<Response> showStationsResponse = 지하철역_조회();
        assertThat(showStationsResponse.jsonPath().getList("name", String.class)).doesNotContain(stationName);
    }
}