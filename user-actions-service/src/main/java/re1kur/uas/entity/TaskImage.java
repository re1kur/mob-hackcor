package re1kur.uas.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "task_image")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "task")
public class TaskImage {

    @Id
    private Long taskId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    private String fileId;

    private String fileUrl;

    private OffsetDateTime expiresAt;
}
