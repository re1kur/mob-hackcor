package re1kur.uas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users_tasks")
public class UserTask {
    @EmbeddedId
    private UserTaskId id;

    @OneToOne
    @JoinColumn(name = "last_attempt_id")
    private TaskAttempt lastAttempt;

    @Column(insertable = false)
    private String status;

}
