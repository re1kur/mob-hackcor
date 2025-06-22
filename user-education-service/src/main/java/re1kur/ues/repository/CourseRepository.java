package re1kur.ues.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re1kur.ues.entity.Course;

import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
}
