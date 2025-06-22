package re1kur.ues.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "flashcards")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flashcard {

    @Id
    private UUID flashcardId;

    private String question;

    private String answer;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}