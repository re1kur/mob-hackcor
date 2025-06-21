package re1kur.uas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re1kur.uas.entity.TaskAttempt;

@Repository
public interface TaskAttemptRepository extends JpaRepository<TaskAttempt, Long> {
}
