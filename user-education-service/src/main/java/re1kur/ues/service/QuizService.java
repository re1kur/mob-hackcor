package re1kur.ues.service;

import re1kur.core.dto.QuestionDto;
import re1kur.core.dto.QuizDto;
import re1kur.core.payload.QuizPayload;

import java.util.List;
import java.util.UUID;

public interface QuizService {
    List<QuestionDto> getQuizQuestions(UUID quizId);

    QuizDto createQuiz(QuizPayload payload);

    QuizDto getQuizByCourse(UUID courseId);
}
