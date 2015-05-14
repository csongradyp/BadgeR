package net.csongradyp.badger.persistence.repository;

import java.util.Optional;
import net.csongradyp.badger.persistence.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<EventEntity, String> {

    Optional<EventEntity> findById(String id);

}
