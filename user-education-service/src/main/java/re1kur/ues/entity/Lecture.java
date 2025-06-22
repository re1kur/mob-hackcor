package re1kur.ues.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "lectures")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lecture {

    @Id
    private UUID lectureId;

    private String title;

    @Lob
    private String content;

    private Integer orderIndex;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}

