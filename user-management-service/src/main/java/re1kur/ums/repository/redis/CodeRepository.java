package re1kur.ums.repository.redis;

import org.springframework.data.repository.CrudRepository;
import re1kur.ums.entity.Code;

public interface CodeRepository extends CrudRepository<Code, String> {
}
