package re1kur.uas.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String shortDescription;

    private String description;

    @Column(insertable = false)
    private OffsetDateTime startsAt;

    private OffsetDateTime endsAt;

    private Integer reward;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "task_id")
    private TaskImage taskImage;
}
