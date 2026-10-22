package com.wallete.wallete_service.event;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.wallete.wallete_service.dao.request.TopUpEventRequest;
import com.wallete.wallete_service.dao.request.TransferEventRequest;
import com.wallete.wallete_service.dao.response.TopUpResponse;
import com.wallete.wallete_service.enums.TransactionStatus;
import com.wallete.wallete_service.enums.WalletStatus;
import com.wallete.wallete_service.model.Wallet;
import com.wallete.wallete_service.repository.WalletRepository;
import com.wallete.wallete_service.service.WalletService;

import lombok.AllArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Service 
@AllArgsConstructor 
public class EventService {
    
    private WalletRepository walletRepository;
    private ObjectMapper objectMapper;
    private WalletService walletService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "User-Wallet")
    public void createWallet(
        String message
    ){
        System.out.println("Create User Wallet Data:-"+message);
        UUID userId = objectMapper.readValue(message, UUID.class);

        System.out.println("Create User Wallet Data:-"+message);
        Wallet newWallet= new Wallet();

       
        newWallet.setUserId(userId);
        newWallet.setCurrency("INR");
        newWallet.setStatus(WalletStatus.ACTIVE);
        
        walletRepository.save(newWallet);
    }

    @KafkaListener(topics = "Top-Up")
    public void  UserTopUp(
        String message
    ){
        TopUpEventRequest userTopUp = objectMapper.readValue(message,TopUpEventRequest.class);

        UUID userId = userTopUp.userId();
        UUID transactionId=userTopUp.transactionId();
        BigDecimal amount = userTopUp.amount();
        
        TopUpResponse result;
        try {
            walletService.topUp(userId, amount);
            result = new TopUpResponse(transactionId,TransactionStatus.SUCCESS,"");
        } catch (Exception e) {
            result= new TopUpResponse(transactionId,TransactionStatus.FAILED,e.getMessage());
            
        }

        publishResult(userId,result);

    }

    @KafkaListener(topics = "Money-Transfer")
    public void  moneyTransfer  (
        String message
    ){
        TransferEventRequest moneyTransfer = objectMapper.readValue(message,TransferEventRequest.class);

        UUID userId = moneyTransfer.senderId();
        UUID transactionId=moneyTransfer.transactionId();
        UUID receverId=moneyTransfer.receverId();
        BigDecimal amount = moneyTransfer.amount();
        
        TopUpResponse result;
        try {
            walletService.moneyTransfer(userId, receverId, amount);
            result = new TopUpResponse(transactionId,TransactionStatus.SUCCESS,"");
        } catch (Exception e) {
            result= new TopUpResponse(transactionId,TransactionStatus.FAILED,e.getMessage());
            
        }

        publishResult(userId,result);

    }


    private void publishResult(UUID userId, TopUpResponse result) {
        String json = objectMapper.writeValueAsString(result);

        kafkaTemplate.send("TOP-UP-RESULT", userId.toString(), json)
            .whenComplete((res, ex) -> {
                if (ex != null) {
                    System.err.print("Failed to publish result for {}"+ result.transactionId());
                } else {
                    System.err.println("Published {} for {}"+ result.status().toString());
                }
            });
    }
}
