package com.disk.share.service;

import com.disk.share.dto.PageResponse;
import com.disk.share.dto.ResourceRequest;
import com.disk.share.dto.ResourceResponse;

import java.util.List;

public interface ResourceService {

    PageResponse<ResourceResponse> getAllResources(int page, int size, String sortBy);

    PageResponse<ResourceResponse> searchResources(String keyword, int page, int size);

    PageResponse<ResourceResponse> getByCategory(String category, int page, int size);

    ResourceResponse getResourceById(Long id);

    List<ResourceResponse> getRecentResources();

    List<ResourceResponse> getPopularResources();

    List<String> getAllCategories();

    ResourceResponse createResource(ResourceRequest request);

    ResourceResponse incrementViews(Long id);

    ResourceResponse incrementDownloads(Long id);

    ResourceResponse rateResource(Long id, Integer score, String sessionId);

    void deleteResource(Long id);

    long countAll();
}
