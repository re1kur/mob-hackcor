package re1kur.uas.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re1kur.core.dto.UserTaskDto;
import re1kur.uas.enums.Status;
import re1kur.uas.service.UserTaskService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-task")
public class UserTaskController {
    private final UserTaskService service;

    @GetMapping
    public List<UserTaskDto> getAll(@RequestParam String userId) {
        return service.getAllByUser(userId);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<UserTaskDto> get(
            @RequestParam String userId,
            @PathVariable Long taskId
    ) {
        return ResponseEntity.ok(service.getById(userId, taskId));
    }

    @PutMapping("/{taskId}/update-status")
    public ResponseEntity<UserTaskDto> updateStatus(
            @RequestParam String userId,
            @PathVariable Long taskId,
            @RequestParam Status status
    ) {
        UserTaskDto body = service.updateStatus(userId, taskId, status.name());
        return ResponseEntity.ok().body(body);
    }

    @DeleteMapping("/{taskId}/delete")
    public ResponseEntity<Void> delete(
            @RequestParam String userId,
            @PathVariable Long taskId
    ) {
        service.delete(userId, taskId);
        return ResponseEntity.ok().build();
    }
}
