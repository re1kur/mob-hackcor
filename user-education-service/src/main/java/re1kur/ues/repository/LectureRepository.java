package re1kur.ues.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re1kur.ues.entity.Lecture;

import java.util.List;
import java.util.UUID;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, UUID> {
    List<Lecture> findByCourseCourseIdOrderByOrderIndex(UUID courseId);
}
