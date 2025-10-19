package de.adorsys.opba.db.repository.jpa;

import de.adorsys.opba.db.domain.entity.sessions.SessionFromAspsp;
import org.springframework.data.repository.CrudRepository;

public interface SessionRepository extends CrudRepository<SessionFromAspsp, Long> {
        SessionFromAspsp findByAuthId(String authId);
    }


