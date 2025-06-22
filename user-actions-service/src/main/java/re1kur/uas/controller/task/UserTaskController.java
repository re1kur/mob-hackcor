package re1kur.uas.controller.task;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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
    public List<UserTaskDto> getAllYours(@AuthenticationPrincipal JwtAuthenticationToken token) {
        return service.getAllByUser(token.getTokenAttributes().get("sub").toString());
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<UserTaskDto> get(
            @AuthenticationPrincipal JwtAuthenticationToken token,
            @PathVariable Long taskId
    ) {
        return ResponseEntity.ok(service.getById(token.getTokenAttributes().get("sub").toString(), taskId));
    }

    @PutMapping("/{taskId}/update-status")
    public ResponseEntity<UserTaskDto> updateStatus(
            @RequestParam String userId,
            @PathVariable Long taskId,
            @RequestParam Status status
    ) {
        service.updateStatus(userId, taskId, status.name());
        return ResponseEntity.ok().build();
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
