package de.adorsys.opba.tppbankingapi.service;

import de.adorsys.opba.db.domain.entity.sessions.SessionFromAspsp;
import de.adorsys.opba.db.repository.jpa.SessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveSession(UUID authId, String sessionKey) {
        SessionFromAspsp session = new SessionFromAspsp();
        session.setAuthId(authId.toString());
        session.setCookie(sessionKey);
        sessionRepository.save(session);
    }
}

