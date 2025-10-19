package de.adorsys.opba.tppbankingapi.service;

import de.adorsys.opba.api.security.internal.config.AuthorizationSessionKeyConfig;
import de.adorsys.opba.db.repository.jpa.SessionRepository;
import de.adorsys.opba.protocol.api.dto.request.FacadeServiceableRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CallbackContextBridge {

    private final AuthorizationSessionKeyConfig.AuthorizationSessionKeyFromHttpRequest authSessionKey;
    private final SessionRepository sessionRepository;

    /**
     * Restores the request scope context for callback processing
     * by recreating the original session's scoped services
     */
    public FacadeServiceableRequest createCallbackRequest(String authId, String redirectState, FacadeServiceableRequest originalTemplate) {
        // The key insight: we need to maintain the SAME authorization context
        // that was used in the original request, not create a new one

        return originalTemplate.toBuilder()
                .authorizationSessionId(authId)
                .redirectCode(redirectState)
                .authorizationKey(sessionRepository.findByAuthId(authId).getCookie())
                .build();
    }

    /**
     * Alternative approach: if we need to create a completely new context
     * for callbacks that don't have an original request context
     */
    public FacadeServiceableRequest createFreshCallbackRequest(String authId, String redirectState, HttpServletRequest request) {
        // This would be used when we can't preserve the original context
        // and need to create a callback-specific context
        return FacadeServiceableRequest.builder()
                .authorizationSessionId(authId)
                .redirectCode(redirectState)
                .authorization("MY-SUPER-FINTECH-ID")
                .sessionPassword("qwerty")
                .authorizationKey(null)
                .build();
    }
}
