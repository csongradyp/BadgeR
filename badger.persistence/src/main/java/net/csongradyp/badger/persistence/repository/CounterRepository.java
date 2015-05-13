package net.csongradyp.badger.persistence.repository;

import java.util.Optional;
import net.csongradyp.badger.persistence.entity.ScoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounterRepository extends JpaRepository<ScoreEntity, String> {

    Optional<ScoreEntity> findById(String id);

}
