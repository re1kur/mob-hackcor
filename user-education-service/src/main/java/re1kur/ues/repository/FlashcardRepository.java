package re1kur.ues.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re1kur.ues.entity.Flashcard;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, UUID> {
    List<Flashcard> findByCourseCourseId(UUID courseId);
}
