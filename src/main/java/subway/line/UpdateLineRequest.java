package subway.line;

public class UpdateLineRequest {
    private final String name;
    private final String color;

    public UpdateLineRequest() {
        this.name = null;
        this.color = null;
    }

    public UpdateLineRequest(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
