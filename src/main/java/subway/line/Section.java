package subway.line;

import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.util.List;

/**
 * 구간은 노선, 상행역, 하행역 정보를 조합했을 때 고유하다
 */
@Entity
@Table(
    name = "section",
    indexes = {
        @Index(
            name = "section_uk_line_up_station_down_station",
            columnList = "lineId, upStationId, downStationId"
        ),
    }
)
public class Section {
    @Comment("구간 ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("노선 ID")
    private Long lineId;

    @Comment("상행역 ID")
    private Long upStationId;

    @Comment("하행역 ID")
    private Long downStationId;

    @Comment("거리")
    private int distance;

    protected Section() {
    }

    protected Section(Long lineId, Long upStationId, Long downStationId, int distance) {
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    private void validate() {
        if (lineId == null) {
            throw new IllegalArgumentException("노선 ID는 빈 값이 올 수 없습니다.");
        }
        if (upStationId == null) {
            throw new IllegalArgumentException("상행역 ID는 빈 값이 올 수 없습니다.");
        }
        if (downStationId == null) {
            throw new IllegalArgumentException("하행역 ID는 빈 값이 올 수 없습니다.");
        }
        if (upStationId == downStationId) {
            throw new IllegalArgumentException("상행역 ID와 하행역 ID는 같을 수 없습니다.");
        }
        if (distance <= 0) {
            throw new IllegalArgumentException("거리는 0보다 커야 합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    public List<Long> getStationIds() {
        return List.of(upStationId, downStationId);
    }

    public static Section create(Long lineId, Long upStationId, Long downStationId, int distance) {
        var newSection = new Section(lineId, upStationId, downStationId, distance);
        newSection.validate();
        return newSection;
    }
}
