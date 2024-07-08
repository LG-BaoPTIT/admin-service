package com.ite.adminservice.service.serviceImpl;

import com.ite.adminservice.security.AESUtil;
import com.ite.adminservice.service.BillService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
public class BillServiceImpl implements BillService {
    private final RestTemplate restTemplate;
    private final String apiKey;

    public BillServiceImpl(RestTemplate restTemplate, @Value("${api.key}") String apiKey) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
    }
    @Override
    public ResponseEntity<?> getBillById(String orderId) throws Exception {
        String url = "http://localhost:8099/api/account/bill/billDetail?orderId=" + orderId;
        String encryptedAPIKey = AESUtil.encrypt(apiKey);
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", encryptedAPIKey);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        String responseData = responseEntity.getBody();
        String decryptData = AESUtil.decrypt(responseData);
        return ResponseEntity.ok(decryptData);}



    @Override
    public ResponseEntity<?> getBill() throws Exception {
        String url = "http://localhost:8099/api/account/bill";
        String encryptedAPIKey = AESUtil.encrypt(apiKey);
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", encryptedAPIKey);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        String responseData = responseEntity.getBody();
        String decryptData = AESUtil.decrypt(responseData);
        return ResponseEntity.ok(decryptData);

    }

    @Override
    public ResponseEntity<?> searchBillss(String orderId, String email, int page, int size) throws Exception {
        String url = "http://localhost:8099/api/account/bill/search?orderId="+orderId +"&email="+email+"&page="+page+"&size="+size;
        String encryptedAPIKey = AESUtil.encrypt(apiKey);
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", encryptedAPIKey);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        String responseData = responseEntity.getBody();
        String decryptData = AESUtil.decrypt(responseData);
        return ResponseEntity.ok(decryptData);

    }

    @Override
    public ResponseEntity<?> getBillRefundById(String orderId) throws Exception {
        String url = "http://localhost:8099/api/account/billRefund/billDetail?orderId=" + orderId;
        String encryptedAPIKey = AESUtil.encrypt(apiKey);
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", encryptedAPIKey); // Thay thế "ApiKey" bằng cách xác định cụ thể của API key
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,String.class);


        String responseData = responseEntity.getBody();
        String decryptData = AESUtil.decrypt(responseData);
        System.out.println(decryptData);
        return ResponseEntity.ok(decryptData);

    }


    @Override
    public ResponseEntity<?> getBillRefund() throws Exception {
        String url = "http://localhost:8099/api/account/billRefund";
        String encryptedAPIKey = AESUtil.encrypt(apiKey);
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", encryptedAPIKey);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

        String responseData = responseEntity.getBody();
        String decryptData = AESUtil.decrypt(responseData);
        System.out.println(decryptData);
        return ResponseEntity.ok(decryptData);

    }

    @Override
    public ResponseEntity<?> searchBillRefund(String orderId, String email, int page, int size) throws Exception {
        String url = "http://localhost:8099/api/account/billRefund/search?orderId=" + orderId + "&email=" + email+"&page="+page+"&size="+size;
        String encryptedAPIKey = AESUtil.encrypt(apiKey);
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", encryptedAPIKey);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> customResponse2 = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        String responseData = customResponse2.getBody();
        String decryptData = AESUtil.decrypt(responseData);
        System.out.println(decryptData);
        return ResponseEntity.ok(decryptData);
    }

}
