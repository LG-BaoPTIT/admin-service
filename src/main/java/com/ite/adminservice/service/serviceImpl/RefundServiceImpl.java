package com.ite.adminservice.service.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ite.adminservice.constants.MessageConstant;
import com.ite.adminservice.entities.Refund;
import com.ite.adminservice.entities.RefundType;
import com.ite.adminservice.payload.data.RefundBody;
import com.ite.adminservice.payload.dto.OrderDTO;
import com.ite.adminservice.payload.request.RefundRequest;
import com.ite.adminservice.repositories.RefundRepository;
import com.ite.adminservice.security.CustomAdminDetails;
import com.ite.adminservice.security.CustomAdminDetailsService;
import com.ite.adminservice.service.RefundService;
import com.ite.adminservice.utils.RefundUtil;
import com.ite.adminservice.utils.ResponseUtil;
import com.ite.adminservice.utils.SignatureUtil;
import io.jsonwebtoken.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@Service
public class RefundServiceImpl implements RefundService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RefundUtil refundUtil;

    @Autowired
    private CustomAdminDetailsService customAdminDetailsService;

    @Autowired
    private RefundRepository refundRepository;

    @Autowired
    private SignatureUtil signatureUtil;

    @Value("${payment.urlRefund}")
    private String urlRefund;

    @Override
    public ResponseEntity<?> refundOrder(HttpServletRequest request, RefundRequest refundRequest) {
        try {

            RestTemplate restTemplate = new RestTemplate();
            String url = "http://127.0.0.1:8099/ecommerce/order/getOrder?orderId=" + refundRequest.getOrderId();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Signature",signatureUtil.signRequest(""));
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<OrderDTO> response = restTemplate.exchange(url, HttpMethod.GET, entity, OrderDTO.class);

            System.out.println(response.getBody());
            OrderDTO orderDTO = response.getBody();
            if(Double.parseDouble(refundRequest.getRefundAmount()) > orderDTO.getTotalAmount()){
                return ResponseUtil.getResponseEntity("01",MessageConstant.INVALID_REFUND_AMOUNT,HttpStatus.BAD_REQUEST);
            }
            Refund refund = Refund.builder()
                    .orderId(orderDTO.getId())
                    .refundAmount(refundRequest.getRefundAmount())
                    .createdDate(Instant.now())
                    .adminId("65eddb02e8fc8939f5c845fe")
                    .reason(refundRequest.getReason())
                    .build();
            if(Double.parseDouble(refundRequest.getRefundAmount()) == orderDTO.getTotalAmount()){
                refund.setRefundType(RefundType.full);
            }else {
                refund.setRefundType(RefundType.partial);
            }
            refundRepository.save(refund);

            RefundBody refundBody = refundUtil.configRefundBody(orderDTO.getTransactionInfo().getTransaction_number(), refund.getId(), refundRequest.getRefundAmount(),orderDTO.getTransactionInfo().getTrans_time(),refund.getRefundType().name());
            String body = refundBody.toString();

            HttpHeaders header = refundUtil.configHeader(body);

            System.out.println(header.toString());
            System.out.println(refundBody.toString());

            HttpEntity<String> requestEntity = new HttpEntity<>(body,header);
            //ResponseEntity<String> responseEntity = restTemplate.postForEntity(urlRefund, requestEntity, String.class);
            ResponseEntity<String> responseEntity = restTemplate.exchange(urlRefund, HttpMethod.POST,requestEntity,String.class);

            String responseBody = responseEntity.getBody();

        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }
}
