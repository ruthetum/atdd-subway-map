package subway.line;

import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import subway.ApiTest;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.line.LineSteps.*;
import static subway.station.StationSteps.지하철역_생성;
import static subway.station.StationSteps.지하철역_생성_요청;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends ApiTest {

    /**
     * Given: 지하철역을 2개 생성하고,
     * Given: 새로운 지하철 노선 정보를 입력하고,
     * When: 관리자가 노선을 생성하면,
     * Then: 해당 노선이 생성되고 노선 목록에 포함된다.
     */
    @DisplayName("지하철 노선을 생성한다")
    @Test
    void createLine() {
        // given
        var 강남역 = 지하철역_생성(지하철역_생성_요청("강남역"));
        var 강남역_ID = 강남역.jsonPath().getLong("id");

        var 신논현역 = 지하철역_생성(지하철역_생성_요청("신논현역"));
        var 신논현역_ID = 신논현역.jsonPath().getLong("id");

        // given
        var 신분당선_생성_요청 = 지하철_노선_생성_요청("신분당선", "bg-red-600", 강남역_ID, 신논현역_ID, 10);

        // when
        var 신분당선 = 지하철_노선_생성(신분당선_생성_요청);

        // then
        assertThat(신분당선.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        var 등록된_신분당선 = 신분당선.jsonPath().getObject("", LineResponse.class);
        등록된_노선_정보_확인(등록된_신분당선, "신분당선", "bg-red-600", 강남역_ID, 신논현역_ID);
    }

    /**
     * TODO
     * - 지하철 노선을 생성할 때 상행역 또는 하행역 ID가 등록되지 않은 경우 요청이 실패한다
     * - 지하철 노선을 생성할 때 상행역과 하행역이 같은 경우 요청이 실패한다
     * - 지하철 노선을 생성할 때 거리가 0 이하인 경우 요청이 실패한다
     * - 지하철 노선을 생성할 때 이름이 없거나 빈 문자열인 경우 요청이 실패한다
     * - 지하철 노선을 생성할 때 색상이 없거나 빈 문자열인 경우 요청이 실패한다
     */

    /**
     * Given: 지하철 노선이 여러 개 등록되어 있고,
     * Given: 여러 개의 지하철 노선이 등록되어 있고,
     * When: 관리자가 지하철 노선 목록을 조회하면,
     * Then: 모든 지하철 노선 목록이 반환된다.
     */
    @DisplayName("지하철 노선 목록을 조회한다")
    @Test
    void showLines() {
        // given
        var 강남역 = 지하철역_생성(지하철역_생성_요청("강남역"));
        var 강남역_ID = 강남역.jsonPath().getLong("id");
        var 신논현역 = 지하철역_생성(지하철역_생성_요청("신논현역"));
        var 신논현역_ID = 신논현역.jsonPath().getLong("id");
        var 역삼역 = 지하철역_생성(지하철역_생성_요청("역삼역"));
        var 역삼역_ID = 역삼역.jsonPath().getLong("id");

        // given
        var 신분당선 = 지하철_노선_생성(지하철_노선_생성_요청("신분당선", "bg-red-600", 강남역_ID, 신논현역_ID, 10));
        var 수도권_2호선 = 지하철_노선_생성(지하철_노선_생성_요청("2호선", "bg-green-600", 강남역_ID, 역삼역_ID, 10));

        // when
        var 지하철_노선_목록 = 지하철_노선_목록_조회();

        // then
        assertThat(지하철_노선_목록.statusCode()).isEqualTo(HttpStatus.OK.value());
        var 등록된_신분당선 = 지하철_노선_목록.jsonPath().getList("", LineResponse.class).stream()
                .filter(line -> line.getName().equals("신분당선"))
                .findFirst()
                .orElseThrow();
        등록된_노선_정보_확인(등록된_신분당선, "신분당선", "bg-red-600", 강남역_ID, 신논현역_ID);

        var 등록된_수도권_2호선 = 지하철_노선_목록.jsonPath().getList("", LineResponse.class).stream()
                .filter(line -> line.getName().equals("2호선"))
                .findFirst()
                .orElseThrow();
        등록된_노선_정보_확인(등록된_수도권_2호선, "2호선", "bg-green-600", 강남역_ID, 역삼역_ID);
    }

    private void 등록된_노선_정보_확인(LineResponse response, String name, String color, Long upStationId, Long downStationId) {
        assertThat(response.getName()).isEqualTo(name);
        assertThat(response.getColor()).isEqualTo(color);
        assertThat(response.getStations().get(0).getId()).isEqualTo(upStationId);
        assertThat(response.getStations().get(1).getId()).isEqualTo(downStationId);
    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 조회하면,
     * Then: 해당 노선의 정보가 반환된다.
     */
    @DisplayName("지하철 노선을 조회한다")
    @Test
    void showLine() {
        //given
        var 신분당선 = 신분당선_등록("신분당선", "bg-red-600");
        var 신분당선_ID = 신분당선.getLeft();
        var 강남역_ID = 신분당선.getMiddle();
        var 신논현역_ID = 신분당선.getRight();

        // when
        var 조회된_신분당선 = 지하철_노선_조회(신분당선_ID);

        // then
        assertThat(조회된_신분당선.statusCode()).isEqualTo(HttpStatus.OK.value());
        var 등록된_신분당선 = 조회된_신분당선.jsonPath().getObject("", LineResponse.class);
        등록된_노선_정보_확인(등록된_신분당선, "신분당선", "bg-red-600", 강남역_ID, 신논현역_ID);
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

    /**
     * 지하철 노선과 노선 등록에 필요한 지하철역을 생성하는 메서드
     * @return <노선 ID, 상행역 ID, 하행역 ID>
     */
    private Triple<Long, Long, Long> 신분당선_등록(String name, String color) {
        var 상행역 = 지하철역_생성(지하철역_생성_요청("상행역"));
        var 상행역_ID = 상행역.jsonPath().getLong("id");
        var 하행역 = 지하철역_생성(지하철역_생성_요청("하행역"));
        var 하행역_ID = 하행역.jsonPath().getLong("id");
        var 지하철_노선 = 지하철_노선_생성_요청(name, color, 상행역_ID, 하행역_ID, 10);
        var 지하철_노선_ID = 지하철_노선_생성(지하철_노선).jsonPath().getLong("id");
        return Triple.of(지하철_노선_ID, 상행역_ID, 하행역_ID);
    }
}
