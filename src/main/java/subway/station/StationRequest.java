package subway.station;

public class StationRequest {
    private final String name;

    public StationRequest() {
        this.name = null;
    }

    public StationRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
