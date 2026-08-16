package com.disk.share.service.impl;

import com.disk.share.dto.PageResponse;
import com.disk.share.dto.RatingRequest;
import com.disk.share.dto.ResourceRequest;
import com.disk.share.dto.ResourceResponse;
import com.disk.share.entity.Rating;
import com.disk.share.entity.Resource;
import com.disk.share.exception.NotFoundException;
import com.disk.share.repository.RatingRepository;
import com.disk.share.repository.ResourceRepository;
import com.disk.share.service.ResourceService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepository;
    private final RatingRepository ratingRepository;

    public ResourceServiceImpl(ResourceRepository resourceRepository, RatingRepository ratingRepository) {
        this.resourceRepository = resourceRepository;
        this.ratingRepository = ratingRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ResourceResponse> getAllResources(int page, int size, String sortBy) {
        Pageable pageable = createPageable(page, size, sortBy);
        Page<Resource> resourcePage = resourceRepository.findByActiveTrue(pageable);
        return mapPage(resourcePage);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ResourceResponse> searchResources(String keyword, int page, int size) {
        Pageable pageable = createPageable(page, size, "createdAt");
        Page<Resource> resourcePage = resourceRepository.search(keyword, pageable);
        return mapPage(resourcePage);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ResourceResponse> getByCategory(String category, int page, int size) {
        Pageable pageable = createPageable(page, size, "createdAt");
        Page<Resource> resourcePage = resourceRepository.findByActiveTrueAndCategoryContainingIgnoreCase(
                category, pageable);
        return mapPage(resourcePage);
    }

    @Override
    @Transactional(readOnly = true)
    public ResourceResponse getResourceById(Long id) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("资源不存在，ID: " + id));
        if (!resource.getActive()) {
            throw new NotFoundException("资源已下架");
        }
        resource.setViews(resource.getViews() + 1);
        resourceRepository.save(resource);
        return toResponse(resource);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResourceResponse> getRecentResources() {
        return resourceRepository.findTop10ByActiveTrueOrderByCreatedAtDesc()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResourceResponse> getPopularResources() {
        return resourceRepository.findTop10ByActiveTrueOrderByDownloadsDesc()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllCategories() {
        return resourceRepository.findAll().stream()
                .map(Resource::getCategory)
                .filter(Objects::nonNull)
                .filter(cat -> !cat.isBlank())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public ResourceResponse createResource(ResourceRequest request) {
        Resource resource = Resource.builder()
                .title(request.getTitle().trim())
                .description(request.getDescription().trim())
                .url(request.getUrl().trim())
                .category(request.getCategory() != null ? request.getCategory().trim() : "")
                .sourceType(request.getSourceType() != null ? request.getSourceType().trim() : "other")
                .views(0)
                .downloads(0)
                .ratingSum(0)
                .ratingCount(0)
                .active(true)
                .build();
        Resource saved = resourceRepository.save(resource);
        return toResponse(saved);
    }

    @Override
    public ResourceResponse incrementViews(Long id) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("资源不存在，ID: " + id));
        resource.setViews(resource.getViews() + 1);
        return toResponse(resourceRepository.save(resource));
    }

    @Override
    public ResourceResponse incrementDownloads(Long id) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("资源不存在，ID: " + id));
        resource.setDownloads(resource.getDownloads() + 1);
        return toResponse(resourceRepository.save(resource));
    }

    @Override
    @Transactional
    public ResourceResponse rateResource(Long id, Integer score, String sessionId) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("资源不存在，ID: " + id));

        Optional<Rating> existing = ratingRepository.findByResourceAndSessionId(resource, sessionId);
        if (existing.isPresent()) {
            Rating rating = existing.get();
            resource.setRatingSum(resource.getRatingSum() - rating.getScore() + score);
            rating.setScore(score);
            ratingRepository.save(rating);
        } else {
            Rating rating = Rating.builder()
                    .score(score)
                    .sessionId(sessionId)
                    .resource(resource)
                    .build();
            ratingRepository.save(rating);
            resource.setRatingCount(resource.getRatingCount() + 1);
            resource.setRatingSum(resource.getRatingSum() + score);
        }
        return toResponse(resourceRepository.save(resource));
    }

    @Override
    public void deleteResource(Long id) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("资源不存在，ID: " + id));
        resource.setActive(false);
        resourceRepository.save(resource);
    }

    @Override
    @Transactional(readOnly = true)
    public long countAll() {
        return resourceRepository.count();
    }

    private Pageable createPageable(int page, int size, String sortBy) {
        Sort sort = switch (sortBy) {
            case "views" -> Sort.by(Sort.Direction.DESC, "views");
            case "downloads" -> Sort.by(Sort.Direction.DESC, "downloads");
            case "rating" -> Sort.by(Sort.Direction.DESC, "avgRating");
            default -> Sort.by(Sort.Direction.DESC, "createdAt");
        };
        return PageRequest.of(Math.max(page, 0), Math.min(size, 50), sort);
    }

    private PageResponse<ResourceResponse> mapPage(Page<Resource> page) {
        List<ResourceResponse> content = page.getContent().stream()
                .map(this::toResponse).collect(Collectors.toList());
        return new PageResponse<>(
                content, page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages(),
                page.isFirst(), page.isLast()
        );
    }

    private ResourceResponse toResponse(Resource resource) {
        double avgRating = resource.getRatingCount() > 0
                ? Math.round((resource.getRatingSum() * 10.0 / resource.getRatingCount())) / 10.0
                : 0.0;
        return new ResourceResponse(
                resource.getId(), resource.getTitle(), resource.getDescription(),
                resource.getUrl(), resource.getCategory(), resource.getSourceType(),
                resource.getViews(), resource.getDownloads(), avgRating,
                resource.getRatingCount(), resource.getActive(),
                resource.getCreatedAt(), resource.getUpdatedAt()
        );
    }
}
