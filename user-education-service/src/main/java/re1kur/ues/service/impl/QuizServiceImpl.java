package re1kur.ues.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import re1kur.core.dto.QuestionDto;
import re1kur.core.dto.QuizDto;
import re1kur.core.payload.QuestionPayload;
import re1kur.core.payload.QuizPayload;
import re1kur.ues.entity.Course;
import re1kur.ues.entity.Question;
import re1kur.ues.entity.QuestionOption;
import re1kur.ues.entity.Quiz;
import re1kur.ues.repository.CourseRepository;
import re1kur.ues.repository.QuestionOptionRepository;
import re1kur.ues.repository.QuestionRepository;
import re1kur.ues.repository.QuizRepository;
import re1kur.ues.service.QuizService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuizRepository repo;
    private final QuestionRepository questionRepo;
    private final QuestionOptionRepository optionRepo;
    private final CourseRepository courseRepo;
    private final QuizRepository quizRepository;

    public List<QuestionDto> getQuizQuestions(UUID quizId) {
        return questionRepo.findByQuizQuizId(quizId).stream().map(q -> {
            List<String> options = optionRepo.findAll().stream()
                    .filter(o -> o.getQuestion().getQuestionId().equals(q.getQuestionId()))
                    .map(QuestionOption::getOptionText)
                    .toList();
            return QuestionDto.builder()
                    .id(q.getQuestionId().toString())
                    .text(q.getText())
                    .options(options)
                    .build();
        }).toList();
    }

    @Override
    public QuizDto createQuiz(QuizPayload payload) {
        Course course = courseRepo.findById(payload.courseId()).orElseThrow();

        List<Question> questions = new ArrayList<>();

        for (int i = 0; i < payload.questions().size(); i++) {
            var q = payload.questions().get(i);

            List<QuestionOption> options = q.options().stream()
                    .map(opt -> QuestionOption.builder().optionText(opt).build())
                    .toList();

            Question question = Question.builder()
                    .text(q.text())
                    .correctAnswerIndex(q.correctOptionIndex())
                    .options(options)
                    .build();

            options.forEach(opt -> opt.setQuestion(question));

            questions.add(question);
        }

        Quiz quiz = Quiz.builder()
                .title(payload.title())
                .course(course)
                .questions(questions)
                .build();

        questions.forEach(q -> q.setQuiz(quiz));


        Quiz saved = quizRepository.save(quiz);

        return mapToDto(saved);
    }

    private QuizDto mapToDto(Quiz saved) {
        return QuizDto.builder()
                .quizId(saved.getQuizId().toString())
                .title(saved.getTitle())
                .passingScore(saved.getPassingScore())
                .questions(saved.getQuestions().stream().map(q -> QuestionDto.builder()
                        .id(q.getQuestionId().toString())
                        .text(q.getText())
                        .options(q.getOptions().stream().map(qp -> qp.getOptionText()).toList())
                        .build()).toList())
                .build();
    }

    @Override
    public QuizDto getQuizByCourse(UUID courseId) {
        return mapToDto(quizRepository.findByCourseCourseId(courseId).orElseThrow());
    }
}