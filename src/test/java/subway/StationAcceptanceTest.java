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
        var 강남역_생성_요청 = 지하철역_생성_요청("강남역");
        var 강남역  = 지하철역_생성(강남역_생성_요청);

        // then
        assertThat(강남역.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        var 지하철역_목록 = 지하철역_조회();
        var 지하철역_이름_목록 = 지하철역_목록.jsonPath().getList("name", String.class);
        assertThat(지하철역_이름_목록).containsAnyOf("강남역");
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
        지하철역_생성(지하철역_생성_요청("강남역"));
        지하철역_생성(지하철역_생성_요청("역삼역"));

        // when: 지하철역 목록 조회
        var 지하철역_목록 = 지하철역_조회();

        // then: 생성되어 있는 2개의 지하철역 응답
        assertThat(지하철역_목록.statusCode()).isEqualTo(HttpStatus.OK.value());
        var 지하철역_이름_목록 = 지하철역_목록.jsonPath().getList("name", String.class);
        assertThat(지하철역_이름_목록).contains("강남역", "역삼역");
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
        var 강남역 = 지하철역_생성(지하철역_생성_요청("강남역"));
        var 강남역_ID = 강남역.jsonPath().getLong("id");

        // when: 지하철역 제거
        var 강남역_제거 = 지하철역_제거(강남역_ID);

        // then: 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
        assertThat(강남역_제거.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        var 지하철역_목록 = 지하철역_조회();
        var 지하철역_이름_목록 = 지하철역_목록.jsonPath().getList("name", String.class);
        assertThat(지하철역_이름_목록).doesNotContain("강남역");
    }
}