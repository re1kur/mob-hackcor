package re1kur.uas.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re1kur.core.dto.TaskDto;
import re1kur.core.payload.DailyTaskPayload;
import re1kur.core.payload.DailyTaskUpdatePayload;
import re1kur.uas.service.TaskService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/task")
public class TaskController {
    private final TaskService service;

    @PostMapping("/create")
    public ResponseEntity<TaskDto> create(@RequestBody @Valid DailyTaskPayload payload) {
        TaskDto body = service.create(payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @GetMapping("/getPage")
    public ResponseEntity<List<TaskDto>> readPage(
            @RequestParam Integer page, @RequestParam Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        List<TaskDto> tasks = service.getDailyTasks(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @GetMapping("/get")
    public ResponseEntity<TaskDto> read(@RequestParam Long id) {
        TaskDto body = service.getById(id);
        return ResponseEntity.status(HttpStatus.FOUND).body(body);
    }

    @PutMapping("/update")
    public ResponseEntity<TaskDto> update(@RequestBody @Valid DailyTaskUpdatePayload payload) {
        TaskDto body = service.update(payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<TaskDto> delete(@RequestParam Long id) {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
