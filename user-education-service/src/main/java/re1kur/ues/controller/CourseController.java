package re1kur.ues.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re1kur.core.FlashcardDto;
import re1kur.core.dto.CourseDto;
import re1kur.core.dto.LectureDto;
import re1kur.core.payload.CoursePayload;
import re1kur.core.payload.LecturePayload;
import re1kur.ues.service.CourseService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<List<CourseDto>> getCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourse(@PathVariable UUID id) {
        return ResponseEntity.ok(courseService.getCourse(id));
    }

    @PostMapping("/create")
    public ResponseEntity<CourseDto> createCourse(@RequestBody CoursePayload payload) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.createCourse(payload));
    }

    @PostMapping("/create-lecture")
    public ResponseEntity<LectureDto> createLecture(@RequestBody LecturePayload payload) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.createLecture(payload));
    }

    @DeleteMapping("/delete-lecture")
    public ResponseEntity<String> deleteLecture(@RequestParam UUID id) {
        courseService.deleteLecture(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}/lectures")
    public ResponseEntity<List<LectureDto>> getLectures(@PathVariable UUID id) {
        return ResponseEntity.ok(courseService.getLecturesByCourse(id));
    }

    @GetMapping("/{id}/flashcards")
    public ResponseEntity<List<FlashcardDto>> getFlashcards(@PathVariable UUID id) {
        return ResponseEntity.ok(courseService.getFlashcards(id));
    }
}

