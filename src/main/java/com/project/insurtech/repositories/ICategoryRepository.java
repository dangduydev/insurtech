package com.project.insurtech.repositories;

import com.project.insurtech.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ICategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {

    // Tìm tất cả các Category dựa trên trạng thái xóa và providerId
    List<Category> findAllByIsDeletedAndProviderId(@Param("isDeleted") Boolean isDeleted,
                                                   @Param("providerId") Long providerId);

    // Tìm Category cụ thể dựa trên id, trạng thái xóa và providerId
    Optional<Category> findByIdAndIsDeletedAndProviderId(@Param("id") Long id,
                                                         @Param("isDeleted") Boolean isDeleted,
                                                         @Param("providerId") Long providerId);

    // Tìm Category dựa trên trạng thái xóa
    List<Category> findAllByIsDeleted(@Param("isDeleted") Boolean isDeleted);
}
