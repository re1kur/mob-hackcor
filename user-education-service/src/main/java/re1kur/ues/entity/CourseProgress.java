package re1kur.ues.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "course_progress")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseProgress {

    @EmbeddedId
    private CourseProgressId id;

    private Integer score;

    private Boolean completed;

    private Instant completedAt;

    private Boolean earnedCertificate;

    private Integer earnedPoints;

    @MapsId("userId")
    @Column(name = "user_id", insertable = false, updatable = false)
    private UUID userId;

    @MapsId("courseId")
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}