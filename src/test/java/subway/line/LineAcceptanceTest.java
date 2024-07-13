package subway.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.ApiTest;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.line.LineSteps.지하철_노선_생성;
import static subway.line.LineSteps.지하철_노선_생성_요청;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends ApiTest {

    /**
     * Given: 새로운 지하철 노선 정보를 입력하고,
     * When: 관리자가 노선을 생성하면,
     * Then: 해당 노선이 생성되고 노선 목록에 포함된다.
     */
    @DisplayName("지하철 노선을 생성한다")
    @Test
    void createLine() {
        // given
        var 신분당선_생성_요청 = 지하철_노선_생성_요청("신분당선", "bg-red-600", 1, 2, 10);

        // when
        var 신분당선 = 지하철_노선_생성(신분당선_생성_요청);

        // then
        assertThat(신분당선.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        var 지하철_노선_이름 = 신분당선.jsonPath().getString("name");
        assertThat(지하철_노선_이름).isEqualTo("신분당선");

        var 지하철_노선_색 = 신분당선.jsonPath().getString("color");
        assertThat(지하철_노선_색).isEqualTo("bg-red-600");

        var 지하철_노선_상행역_ID = 신분당선.jsonPath().getList("stations", CreateLineResponse.StationDto.class).get(0).getId();
        assertThat(지하철_노선_상행역_ID).isEqualTo(1);

        var 지하철_노선_하행역_ID = 신분당선.jsonPath().getList("stations", CreateLineResponse.StationDto.class).get(1).getId();
        assertThat(지하철_노선_하행역_ID).isEqualTo(2);
    }

    /**
     * Given: 여러 개의 지하철 노선이 등록되어 있고,
     * When: 관리자가 지하철 노선 목록을 조회하면,
     * Then: 모든 지하철 노선 목록이 반환된다.
     */
    @DisplayName("지하철 노선 목록을 조회한다")
    @Test
    void showLines() {
        // TODO: 지하철 노선 목록 조회
    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 조회하면,
     * Then: 해당 노선의 정보가 반환된다.
     */
    @DisplayName("지하철 노선을 조회한다")
    @Test
    void showLine() {
        // TODO: 지하철 노선 조회
    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 수정하면,
     * Then: 해당 노선의 정보가 수정된다.
     */
    @DisplayName("지하철 노선을 수정한다")
    @Test
    void updateLine() {
        // TODO: 지하철 노선 수정
    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 삭제하면,
     * Then: 해당 노선이 삭제되고 노선 목록에서 제외된다.
     */
    @DisplayName("지하철 노선을 삭제한다")
    @Test
    void deleteLine() {
        // TODO: 지하철 노선 삭제
    }
}
