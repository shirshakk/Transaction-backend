package com.user.user_service.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.user.user_service.dao.request.UserProfileRequest;
import com.user.user_service.dao.response.UserProfileResponse;
import com.user.user_service.dao.response.UserRecommentaionResponse;
import com.user.user_service.model.User;
import com.user.user_service.repository.UserProfileRepository;

@Service 
public class UserService {
    private final UserProfileRepository userProfileRepository;

    public UserService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }


    public UserProfileResponse getUserProfile(UUID userId) {
        User user = userProfileRepository.getReferenceById(userId);
        return new UserProfileResponse(
            user.getFullName(),
            user.getEmail(),
            user.getPhoneNumber(),
            user.getDateOfBirth(),
            user.getProfileImageUrl()
        );

    }

    public void updateUserProfile(UUID userId, UserProfileRequest request) {
        User user = userProfileRepository.getReferenceById(userId);
        user.setFullName(request.name());
        user.setEmail(request.email());
        user.setPhoneNumber(request.phone());
        user.setDateOfBirth(request.dob());
        user.setProfileImageUrl(request.profileImageUrl());
        userProfileRepository.save(user);
    }


    public List<UserRecommentaionResponse> searchUser(String request){
        List<User> searchUser = userProfileRepository.findByFullNameStartingWithIgnoreCaseOrPhoneNumberStartingWith(request, request, PageRequest.of(0, 10));
        List<UserRecommentaionResponse> getSearchUser = new ArrayList<>();
        for(User user : searchUser){
            getSearchUser.add(new UserRecommentaionResponse(
                user.getFullName(),
                user.getUserId(),
                user.getPhoneNumber()
            ));
        }

        return getSearchUser;
    }
}
