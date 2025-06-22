package re1kur.ums.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_information")
public class UserInformation {

    @Id
    private UUID userId;

    private String firstname;

    private String lastname;

    private Integer rating;

    private String level;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
}
