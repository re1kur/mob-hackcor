package re1kur.uas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re1kur.uas.entity.DailyTask;

@Repository
public interface DailyTaskRepository extends JpaRepository<DailyTask, Long> {
    Boolean existsByTitle(String title);
}
