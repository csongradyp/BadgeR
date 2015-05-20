package net.csongradyp.badger.persistence.repository;

import net.csongradyp.badger.persistence.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<EventEntity, String> {

}
