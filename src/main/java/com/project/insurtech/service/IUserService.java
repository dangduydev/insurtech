package com.project.insurtech.service;

import com.project.insurtech.dtos.ProviderDTO;
import com.project.insurtech.dtos.UserDTO;
import com.project.insurtech.entities.User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;

    String login(String phoneNumber, String password, Long roleId) throws Exception;

    User createProvider(ProviderDTO providerDTO) throws Exception;
}
