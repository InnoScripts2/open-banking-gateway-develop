package de.adorsys.opba.tppbankingapi.service;

import de.adorsys.opba.consentapi.service.FromAspspMapper;
import de.adorsys.opba.protocol.api.dto.request.FacadeServiceableRequest;
import de.adorsys.opba.protocol.api.dto.request.authorization.AuthorizationRequest;
import de.adorsys.opba.protocol.api.dto.request.authorization.fromaspsp.FromAspspRequest;
import de.adorsys.opba.protocol.facade.services.authorization.FromAspspRedirectHandler;
import de.adorsys.opba.protocol.facade.services.authorization.GetAuthorizationStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
@ComponentScan({
        "de.adorsys.opba.protocol.facade.services.pis",
        "de.adorsys.opba.protocol.facade.services.psu",
        "de.adorsys.opba.protocol.facade.services.authorization",
        "de.adorsys.opba.consentapi",
        "de.adorsys.opba.tppbankingapi.service"
})
public class CallbackService {

    private final FromAspspMapper aspspMapper;
    private final FromAspspRedirectHandler fromAspspRedirectHandler;
    private final FacadeServiceableRequest serviceableTemplate;
    private final CallbackContextBridge contextBridge;
    private final GetAuthorizationStateService authorizationStateService;

    public CompletableFuture callBackSuccess(String authId, String redirectState) {
        FacadeServiceableRequest facadeRequest = contextBridge.createCallbackRequest(
                authId,
                redirectState,
                serviceableTemplate
        );
         return fromAspspRedirectHandler.execute(
                FromAspspRequest.builder()
                        .facadeServiceable(facadeRequest)
                        .isOk(true)
                        .code(null)
                        .build()
        ).thenApply(aspspMapper::translate).thenCompose(listHeaders -> {
            String redirectCode = listHeaders.getHeaders().getFirst("X-XSRF-TOKEN");
            return authorizationStateService.execute(AuthorizationRequest.builder()
                    .facadeServiceable(facadeRequest.toBuilder()
                            .redirectCode(redirectCode)
                            .build())
                    .build());
        });

    }

    public CompletableFuture callBackFailure(String authId, String redirectState) {

        FacadeServiceableRequest facadeRequest = contextBridge.createCallbackRequest(
                authId,
                redirectState,
                serviceableTemplate
        );
        return fromAspspRedirectHandler.execute(
                FromAspspRequest.builder()
                        .facadeServiceable(facadeRequest)
                        .isOk(false)
                        .build()
        ).thenApply(aspspMapper::translate);
    }
}
