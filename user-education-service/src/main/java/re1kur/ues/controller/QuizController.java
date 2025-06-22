package re1kur.ues.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re1kur.core.dto.QuestionDto;
import re1kur.core.dto.QuizDto;
import re1kur.core.payload.QuizPayload;
import re1kur.ues.service.QuizService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @GetMapping("/{quizId}/questions")
    public ResponseEntity<List<QuestionDto>> getQuestions(@PathVariable UUID quizId) {
        return ResponseEntity.ok(quizService.getQuizQuestions(quizId));
    }

    @PostMapping
    public ResponseEntity<QuizDto> createQuiz(@RequestBody QuizPayload payload) {
        return ResponseEntity.ok(quizService.createQuiz(payload));
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<QuizDto> getQuiz(@PathVariable UUID courseId) {
        return ResponseEntity.ok(quizService.getQuizByCourse(courseId));
    }
}
