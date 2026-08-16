package com.disk.share.repository;

import com.disk.share.entity.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

    Page<Resource> findByActiveTrue(Pageable pageable);

    Page<Resource> findByActiveTrueAndCategoryContainingIgnoreCase(
            String category, Pageable pageable);

    Page<Resource> findByActiveTrueAndTitleContainingIgnoreCaseOrActiveTrueAndDescriptionContainingIgnoreCase(
            String title, String description, Pageable pageable);

    @Query("SELECT r FROM Resource r WHERE r.active = true AND " +
           "(LOWER(r.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(r.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Resource> search(@Param("keyword") String keyword, Pageable pageable);

    List<Resource> findTop10ByActiveTrueOrderByCreatedAtDesc();

    List<Resource> findTop10ByActiveTrueOrderByDownloadsDesc();
}
