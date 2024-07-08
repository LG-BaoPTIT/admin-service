package com.ite.adminservice.utils;

import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;

@Service
public class RequestIdGenerator {
    public String generateRequestId() {
        // Lấy thời gian hiện tại
        Instant now = Instant.now();

        // Chuyển đổi thời gian thành chuỗi
        String timeString = Long.toString(now.toEpochMilli());

        // Tạo một số ngẫu nhiên
        SecureRandom random = new SecureRandom();
        int randomNumber = random.nextInt(1000000); // Số ngẫu nhiên từ 0 đến 999999

        // Kết hợp thời gian và số ngẫu nhiên
        String combinedString = timeString + randomNumber;

        // Băm chuỗi kết hợp để tạo request ID
        String requestId = hashString(combinedString);

        return requestId;
    }

    private static String hashString(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());

            // Chuyển byte array thành chuỗi hex
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}

