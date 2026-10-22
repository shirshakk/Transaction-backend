package com.user.user_service.event;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.user.user_service.dao.request.UserProfileRequest;
import com.user.user_service.model.User;
import com.user.user_service.repository.UserProfileRepository;

import tools.jackson.databind.ObjectMapper;

@Service 
public class UserRegisterConsumer {

    private final ObjectMapper objectMapper;
    private UserProfileRepository userProfileRepository;

     public UserRegisterConsumer(ObjectMapper objectMapper, UserProfileRepository userProfileRepository) {
        this.objectMapper = objectMapper;
        this.userProfileRepository = userProfileRepository;
    }

    @KafkaListener(topics = "user-registered")
    public void consumeUserRegistered(String message) {

         System.out.println("RAW KAFKA MESSAGE: " + message);
        

        UserProfileRequest event = null;
        try {
            event = objectMapper.readValue(message, UserProfileRequest.class);
            System.out.println("Event userId: " + event.user_id());



            User userProfileRequest = new User();
            userProfileRequest.setUserId(event.user_id());
            userProfileRequest.setFullName(event.name());   
            userProfileRequest.setEmail(event.email());
            userProfileRequest.setPhoneNumber(event.phone());
            userProfileRequest.setDateOfBirth(event.dob());
            userProfileRequest.setProfileImageUrl(event.profileImageUrl());
            
            userProfileRepository.save(userProfileRequest);
            System.out.println("Received User Registered Event: " + event);
        } catch (Exception e) {
            System.err.println("Error processing user registered event: " + e.getMessage());
        }

    }
    
}
