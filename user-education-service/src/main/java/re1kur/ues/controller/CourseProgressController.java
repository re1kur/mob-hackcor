package re1kur.ues.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re1kur.core.dto.CourseProgressDto;
import re1kur.ues.service.CourseProgressService;

import java.util.UUID;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class CourseProgressController {

    private final CourseProgressService progressService;

    @PostMapping("/{courseId}/lecture")
    public ResponseEntity<CourseProgressDto> completeLecture(@RequestParam UUID userId,
                                                             @PathVariable UUID courseId) {
        return ResponseEntity.ok(progressService.markLectureCompleted(userId, courseId));
    }

    @PostMapping("/{courseId}/quiz")
    public ResponseEntity<CourseProgressDto> submitQuiz(@RequestParam UUID userId,
                                                        @PathVariable UUID courseId,
                                                        @RequestParam int score) {
        return ResponseEntity.ok(progressService.markQuizPassed(userId, courseId, score));
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CourseProgressDto> getProgress(@RequestParam UUID userId,
                                                         @PathVariable UUID courseId) {
        return ResponseEntity.ok(progressService.getProgress(userId, courseId));
    }

    @PostMapping("/{courseId}/start")
    public ResponseEntity<CourseProgressDto> startCourse(@RequestParam UUID userId,
                                                         @PathVariable UUID courseId) {
        return ResponseEntity.ok(progressService.startCourse(userId, courseId));
    }
}
