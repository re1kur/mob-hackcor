package re1kur.uas.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re1kur.core.dto.DailyTaskDto;
import re1kur.core.payload.DailyTaskPayload;
import re1kur.core.payload.DailyTaskUpdatePayload;
import re1kur.uas.service.DailyTaskService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/daily-task/")
public class DailyTaskController {
    private final DailyTaskService service;

    @PostMapping("create")
    public ResponseEntity<DailyTaskDto> create(@RequestBody @Valid DailyTaskPayload payload) {
        DailyTaskDto body = service.create(payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @GetMapping("getPage")
    public ResponseEntity<List<DailyTaskDto>> readPage(
            @RequestParam Integer page, @RequestParam Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        List<DailyTaskDto> tasks = service.getDailyTasks(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @GetMapping("get")
    public ResponseEntity<DailyTaskDto> read(@RequestParam Long id) {
        DailyTaskDto body = service.getById(id);
        return ResponseEntity.status(HttpStatus.FOUND).body(body);
    }

    @PutMapping("update")
    public ResponseEntity<DailyTaskDto> update(@RequestBody @Valid DailyTaskUpdatePayload payload) {
        DailyTaskDto body = service.update(payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @DeleteMapping("delete")
    public ResponseEntity<DailyTaskDto> delete(@RequestParam Long id) {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
