package re1kur.ues.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import re1kur.ues.entity.Question;

import java.util.List;
import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID> {
    List<Question> findByQuizQuizId(UUID quizId);
}
