package re1kur.ums.repository.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import re1kur.ums.entity.RefreshToken;

@Repository
public interface TokenRepository extends CrudRepository<RefreshToken, String> {
}
