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
    public LineResponse saveLine(CreateLineRequest request) {
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

    /**
     * TODO. 요구사항 수정될 수 있음. 연관관계 설정은 추후 진행, 이에 따른 조회 방법 수정 필요
     */
    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(line -> {
                    var stations = this.getStationsFromLine(line);
                    return new LineResponse(
                            line.getId(),
                            line.getName(),
                            line.getColor(),
                            stations.stream()
                                    .map(station -> new LineResponse.StationDto(station.getId(), station.getName()))
                                    .collect(Collectors.toList())
                    );
                })
                .collect(Collectors.toList());
    }

    public LineResponse findLineById(Long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선입니다."));
        var stations = this.getStationsFromLine(line);
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                stations.stream()
                        .map(station -> new LineResponse.StationDto(station.getId(), station.getName()))
                        .collect(Collectors.toList())
        );
    }

    public List<Station> getStationsFromLine(Line line) {
        var sections = sectionRepository.findAllByLineId(line.getId());
        return stationRepository.findAllById(
                sections.stream()
                        .flatMap(section -> section.getStationIds().stream())
                        .collect(Collectors.toList())
        );
    }

    @Transactional
    public void updateLine(Long id, UpdateLineRequest request) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선입니다."));
        line.update(request.getName(), request.getColor());
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }
}
