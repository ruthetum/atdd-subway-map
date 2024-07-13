package subway.line;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class LineController {

    private LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<CreateLineResponse> createLine(
            @RequestBody CreateLineRequest request
    ) {
        CreateLineResponse newLine = lineService.saveLine(request);
        return ResponseEntity.created(URI.create("/stations/" + newLine.getId())).body(newLine);
    }
}
