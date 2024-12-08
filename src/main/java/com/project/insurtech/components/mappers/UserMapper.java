package com.project.insurtech.components.mappers;

import com.project.insurtech.dtos.UserDTO;
import com.project.insurtech.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "retypePassword", ignore = true)
    @Mapping(target = "roleId", ignore = true)
    @Mapping(source = "id", target = "id")
    UserDTO fromEntityToDTO(User user);
}
