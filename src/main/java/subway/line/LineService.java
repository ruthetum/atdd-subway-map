package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.Station;
import subway.station.StationRepository;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    // TODO. 도메인 접근을 위한 과도한 직접 접근은 추후 리팩토링을 통해 제거한다
    private LineRepository lineRepository;
    private StationRepository stationRepository;
    private SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest request) {
        // 지하철역 조회 (upStationId, downStationId)
        var targetStationIds = List.of(request.getUpStationId(), request.getDownStationId());
        var stations = stationRepository.findAllById(targetStationIds);
        if (stations.size() != targetStationIds.size()) {
            throw new IllegalArgumentException("존재하지 않는 역이 포함되어 있습니다.");
        }

        // 노선 생성
        Line line = lineRepository.save(Line.create(request.getName(), request.getColor()));

        // 구간 생성
        sectionRepository.save(
                Section.create(
                    line.getId(),
                    request.getUpStationId(),
                    request.getDownStationId(),
                    request.getDistance()
                )
        );

        var findStationMap = stations.stream()
            .collect(Collectors.toMap(Station::getId, Function.identity()));

        // 현재는 상행역, 하행역 2개로 고정해서 반환한다
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                List.of(
                    new LineResponse.StationDto(
                        findStationMap.get(request.getUpStationId()).getId(),
                        findStationMap.get(request.getUpStationId()).getName()
                    ),
                    new LineResponse.StationDto(
                        findStationMap.get(request.getDownStationId()).getId(),
                        findStationMap.get(request.getDownStationId()).getName()
                    )
                )
        );
    }
}
