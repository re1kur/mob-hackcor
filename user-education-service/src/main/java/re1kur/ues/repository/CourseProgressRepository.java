package re1kur.ues.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re1kur.ues.entity.CourseProgress;
import re1kur.ues.entity.CourseProgressId;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourseProgressRepository extends JpaRepository<CourseProgress, CourseProgressId> {
    List<CourseProgress> findByIdUserId(UUID userId);
}
