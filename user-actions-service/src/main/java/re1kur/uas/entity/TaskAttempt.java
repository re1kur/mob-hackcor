package re1kur.uas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "task_attempts")
public class TaskAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;


    @Column(insertable = false)
    private LocalDateTime attemptTime;

    private String textContent;

    private UUID fileContentId;

    @Column(insertable = false)
    private Boolean confirmed;

    @Column(insertable = false)
    private UUID moderatorId;
}
