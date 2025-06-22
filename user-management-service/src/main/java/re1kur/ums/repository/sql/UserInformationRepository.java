package re1kur.ums.repository.sql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re1kur.ums.entity.UserInformation;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserInformationRepository extends JpaRepository<UserInformation, UUID> {
    Optional<UserInformation> findByUserId(UUID userId);
}
