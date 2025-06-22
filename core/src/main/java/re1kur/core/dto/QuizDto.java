package re1kur.core.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record QuizDto (
        String quizId,
        String title,
        Integer passingScore,
        String courseId,
        List<QuestionDto> questions
) {
}
