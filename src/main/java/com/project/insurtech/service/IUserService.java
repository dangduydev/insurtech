package com.project.insurtech.service;

import com.project.insurtech.dtos.ProviderDTO;
import com.project.insurtech.dtos.UserDTO;
import com.project.insurtech.entities.User;
import com.project.insurtech.responses.User.UserDetailResponse;

public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;

    String login(String phoneNumber, String password, Long roleId) throws Exception;

    User createProvider(ProviderDTO providerDTO) throws Exception;

    UserDetailResponse getUserDetail(Long id) throws Exception;
}
