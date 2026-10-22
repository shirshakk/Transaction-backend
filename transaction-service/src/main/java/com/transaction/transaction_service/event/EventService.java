package com.transaction.transaction_service.event;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.transaction.transaction_service.dao.request.TopUpEvent;
import com.transaction.transaction_service.dao.request.TransferEventRequest;
import com.transaction.transaction_service.dao.response.TopUpResponse;
import com.transaction.transaction_service.enums.TransactionStatus;
import com.transaction.transaction_service.service.UpdateTransaction;

import tools.jackson.databind.ObjectMapper;

@Service 
public class EventService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final UpdateTransaction updateTransaction;

    public EventService(
            KafkaTemplate<String, String> kafkaTemplate,ObjectMapper objectMapper,UpdateTransaction updateTransaction) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.updateTransaction=updateTransaction;
    }
    
    public void userTopUp(UUID transactionId,UUID userId,BigDecimal amount){
        TopUpEvent topUP = new TopUpEvent(transactionId,userId,amount);
        try {
            String eventJson = objectMapper.writeValueAsString(topUP);
            System.out.println("SENDING KAFKA MESSAGE: " + eventJson);
            kafkaTemplate.send("Top-Up",eventJson)
            .whenComplete((result,exception)->{
                 if (exception != null) {
                            updateTransaction.transactionUpdate(transactionId, TransactionStatus.FAILED);
                        } else {
                            System.out.println(
                                    "User Top Up event published: "
                                            + userId);
                        }
            });
        } catch (Exception e) {
            updateTransaction.transactionUpdate(transactionId, TransactionStatus.FAILED);
                        
        }
    
    }


    public void transfer(TransferEventRequest request){
        try {
            String eventJson = objectMapper.writeValueAsString(request);
            System.out.println("SENDING KAFKA MESSAGE: " + eventJson);
            kafkaTemplate.send("Money-Transfer",eventJson)
            .whenComplete((result,exception)->{
                 if (exception != null) {
                            updateTransaction.transactionUpdate(request.transactionId(), TransactionStatus.FAILED);
                        } else {
                            System.out.println(
                                    "Transaction event published: "
                                            + request.transactionId());
                        }
            });
        } catch (Exception e) {
            updateTransaction.transactionUpdate(request.transactionId(), TransactionStatus.FAILED);
             
        }
    }



    @KafkaListener(topics = "TOP-UP-RESULT")
    public void transactionUpdate(String message){
        TopUpResponse userTopUp = objectMapper.readValue(message,TopUpResponse.class);
        UUID transactionId = userTopUp.transactionId();
        TransactionStatus tStatus= userTopUp.status();
        
        updateTransaction.transactionUpdate(transactionId, tStatus);
        

    }


    
}
