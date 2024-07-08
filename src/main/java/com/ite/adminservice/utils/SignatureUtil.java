package com.ite.adminservice.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class SignatureUtil {
    @Value("${api.key}")
    private String key;
    public boolean verifySignature(HttpServletRequest request, String payload){
        try {

//            String accessToken = null;
//            String authorizationHeader = request.getHeader("Authorization");
//
//            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//                accessToken = authorizationHeader.substring(7);
//            }
////            String header = extractHeaders(request);
////            String method = request.getMethod();
////            String uri = request.getRequestURI();
////            String payload = extractPayload(request);
            String receivedSignature = request.getHeader("Signature");
            String calculatedSignature = signRequest(payload);
            System.out.println(calculatedSignature);
            return receivedSignature != null && receivedSignature.equals(calculatedSignature);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String signRequest(String payload) throws NoSuchAlgorithmException, InvalidKeyException {
        String requestData = payload;
        SecretKeySpec secretKeySpec = new SecretKeySpec(this.key.getBytes(), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);
        byte[] signatureBytes = mac.doFinal(requestData.getBytes());
        return Base64.getEncoder().encodeToString(signatureBytes);
    }
}
