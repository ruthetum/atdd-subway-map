package subway.line;

import java.util.List;

public class LineResponse {

    public static class StationDto {
        private Long id;
        private String name;

        public StationDto() {
        }

        public StationDto(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    private Long id;
    private String name;
    private String color;
    private List<StationDto> stations;

    public LineResponse() {
    }

    public LineResponse(Long id, String name, String color, List<StationDto> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<StationDto> getStations() {
        return stations;
    }
}
