package subway.line;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class LineController {

    private LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(
            @RequestBody CreateLineRequest request
    ) {
        LineResponse newLine = lineService.saveLine(request);
        return ResponseEntity.created(URI.create("/stations/" + newLine.getId())).body(newLine);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity.ok().body(lineService.findAllLines());
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponse> showLine(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok().body(lineService.findLineById(id));
    }
}
