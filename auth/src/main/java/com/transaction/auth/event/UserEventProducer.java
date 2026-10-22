package com.transaction.auth.event;

import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.transaction.auth.DAO.request.UserProfile;

import tools.jackson.databind.ObjectMapper;

@Service 
public class UserEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

     public UserEventProducer(
            KafkaTemplate<String, String> kafkaTemplate,ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

     public void publishUserRegistered(UserProfile userProfile) {

        try{
            String eventJson = objectMapper.writeValueAsString(userProfile);
            System.out.println("SENDING KAFKA MESSAGE: " + eventJson);
            kafkaTemplate.send("user-registered", eventJson)
                    .whenComplete((result, exception) -> {
                        if (exception != null) {
                            System.err.println(
                                    "Failed to publish user event: "
                                            + exception.getMessage());
                        } else {
                            System.out.println(
                                    "User registration event published: "
                                            + userProfile.user_id());
                        }
                    });
        }catch (Exception e) {
            System.err.println(
                    "Failed to serialize user event: "
                            + e.getMessage());
        }
    }

    public void userRegisterWallet(UUID userId){
        
        try {
            String event = objectMapper.writeValueAsString(userId);
            kafkaTemplate.send("User-Wallet",event)
                .whenComplete((result,exception) ->{
                    if(exception != null){
                        System.err.println(
                                    "Failed to publish user Wallet event: "
                                            + exception.getMessage());
                    }else{
                        System.out.println(
                                    "User Wallet registration event published: "
                                            + userId);
                    }
                });
        } catch (Exception e) {
             System.err.println(
                    "Failed to serialize user event: "
                            + e.getMessage());
        }
    }
}
