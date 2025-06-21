package re1kur.uas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re1kur.uas.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Boolean existsByTitle(String title);
}
