package re1kur.ues.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re1kur.ues.entity.QuestionOption;

import java.util.UUID;

@Repository
public interface QuestionOptionRepository extends JpaRepository<QuestionOption, UUID> {}
