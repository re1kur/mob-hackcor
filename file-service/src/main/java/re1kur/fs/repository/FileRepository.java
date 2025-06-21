package re1kur.fs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re1kur.fs.entity.File;

import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<File, UUID> {
}
