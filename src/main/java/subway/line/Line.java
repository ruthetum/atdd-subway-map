package subway.line;

import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Entity
public class Line {
    @Comment("지하철 노선 ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("지하철 노선 이름")
    @Column(length = 20, nullable = false)
    private String name;

    @Comment("지하철 노선 색상")
    @Column(length = 20, nullable = false)
    private String color;

    protected Line() {
    }

    protected Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    private void validate() {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("지하철 노선 이름은 빈 값이 올 수 없습니다.");
        }
        if (color == null || color.isBlank()) {
            throw new IllegalArgumentException("지하철 노선 색상은 빈 값이 올 수 없습니다.");
        }
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

    public static Line create(String name, String color) {
        var newLine = new Line(name, color);
        newLine.validate();
        return newLine;
    }
}
