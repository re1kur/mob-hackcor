package re1kur.ues.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re1kur.ues.entity.*;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, UUID> {
    Optional<Quiz> findByCourseCourseId(UUID courseId);
}

