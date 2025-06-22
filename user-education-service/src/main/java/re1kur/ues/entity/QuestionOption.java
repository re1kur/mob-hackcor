package re1kur.ues.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "question_options")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionOption {

    @Id
    private UUID optionId;

    private String optionText;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
}

