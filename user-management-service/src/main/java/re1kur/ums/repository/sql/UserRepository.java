package re1kur.ums.repository.sql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import re1kur.ums.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @Query(value = "SELECT * FROM users u JOIN user_information ui ON u.id = ui.user_id ORDER BY ui.rating DESC LIMIT :size", nativeQuery = true)
    List<User> findAllOrderByRating(@Param("size") Integer size);
}
