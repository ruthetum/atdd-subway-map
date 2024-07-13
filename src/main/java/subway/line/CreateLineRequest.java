package subway.line;

public class CreateLineRequest {
    private final String name;
    private final String color;
    private final int upStationId;
    private final int downStationId;
    private final int distance;

    public CreateLineRequest() {
        this.name = null;
        this.color = null;
        this.upStationId = 0;
        this.downStationId = 0;
        this.distance = 0;
    }

    public CreateLineRequest(String name, String color, int upStationId, int downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getUpStationId() {
        return upStationId;
    }

    public int getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }
}
