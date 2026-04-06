package com.euphoria.demo.utils;

import com.euphoria.demo.exception.CustomDataException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class GoogleUtils {
    @Value("${google.client.id}")
    private String googleClientId;

    public GoogleIdToken.Payload verifyGoogleToken(String token) throws Exception {

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                new GsonFactory()
        )
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        GoogleIdToken idToken = verifier.verify(token);

        if (idToken != null) {
            return idToken.getPayload();
        } else {
            throw new CustomDataException("Invalid token");
        }
    }
}
