package re1kur.uas.controller.task;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re1kur.core.dto.TaskAttemptDto;
import re1kur.core.payload.TaskAttemptPayload;
import re1kur.core.payload.TaskAttemptUpdatePayload;
import re1kur.uas.service.TaskAttemptService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/task-attempt")
public class TaskAttemptController {
    private final TaskAttemptService service;

    @PostMapping("/create")
    public ResponseEntity<TaskAttemptDto> create(@RequestBody TaskAttemptPayload payload) {
        TaskAttemptDto body = service.create(payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @GetMapping("/get")
    public ResponseEntity<TaskAttemptDto> read(@RequestParam Long id) {
        TaskAttemptDto body = service.getById(id);
        return ResponseEntity.status(HttpStatus.FOUND).body(body);
    }

    @PutMapping("/update")
    public ResponseEntity<TaskAttemptDto> update(@RequestBody TaskAttemptUpdatePayload payload) {
        TaskAttemptDto body = service.update(payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam Long id) {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
