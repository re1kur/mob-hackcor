package re1kur.uas.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import re1kur.uas.entity.UserTaskId;
import re1kur.uas.entity.UserTask;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserTaskRepository extends JpaRepository<UserTask, UserTaskId> {
    @Query("""
        SELECT u FROM UserTask u
        WHERE u.id.userId = :userId
    """)
    List<UserTask> findAllByIdUserId(@Param("userId") UUID userId);

    @Modifying
    @Transactional
    @Query("""
        UPDATE UserTask u
        SET u.status = :status
        WHERE u.id.userId = :userId AND u.id.taskId = :taskId
    """)
    UserTask updateStatus(@Param("userId") UUID userId,
                          @Param("taskId") Long taskId,
                          @Param("status") String status);
}
