package de.adorsys.opba.tppbankingapi.controller;

import de.adorsys.opba.tppbankingapi.callback.model.generated.ConsentAuth;
import de.adorsys.opba.tppbankingapi.callback.resource.generated.CallbackFromAspspWithoutConsentUiApi;
import de.adorsys.opba.tppbankingapi.service.CallbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TppBankingCallBackController implements CallbackFromAspspWithoutConsentUiApi {

    private final CallbackService callbackService;

    @Override
    public ResponseEntity<ConsentAuth> callBackSuccess(String authId, String aspspRedirectCode) {

        callbackService.callBackSuccess(authId, aspspRedirectCode);
        return null;
    }

    @Override
    public ResponseEntity<ConsentAuth> callBackFailure(String authId, String aspspRedirectCode) {
        callbackService.callBackFailure(authId, aspspRedirectCode);
        return null;
    }







}
