package com.project.insurtech.components.mappers;

import com.project.insurtech.dtos.CategoryDTO;
import com.project.insurtech.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface CategoryMapper {

    @Mapping(target = "isDeleted", ignore = true)
    Category fromDTOtoEntity(CategoryDTO categoryDTO);

    CategoryDTO fromEntityToDTO(Category category);

    // Thêm phương thức để cập nhật entity từ DTO
    void updateEntityFromDto(CategoryDTO categoryDTO, @MappingTarget Category category);
}
