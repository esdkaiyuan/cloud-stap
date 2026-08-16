package com.disk.share.repository;

import com.disk.share.entity.Rating;
import com.disk.share.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    Optional<Rating> findByResourceAndSessionId(Resource resource, String sessionId);

    int countByResource(Resource resource);
}
