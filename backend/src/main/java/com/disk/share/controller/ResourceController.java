package com.disk.share.controller;

import com.disk.share.dto.ApiResponse;
import com.disk.share.dto.PageResponse;
import com.disk.share.dto.ResourceRequest;
import com.disk.share.dto.ResourceResponse;
import com.disk.share.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/resources")
@Validated
@Tag(name = "资源管理", description = "网盘资源的增删改查及评分接口")
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping
    @Operation(summary = "分页获取资源列表", description = "支持排序：createdAt(默认), views, downloads, rating")
    public ApiResponse<PageResponse<ResourceResponse>> getAllResources(
            @Parameter(description = "页码，从0开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "排序字段") @RequestParam(defaultValue = "createdAt") String sortBy) {
        return ApiResponse.success(resourceService.getAllResources(page, size, sortBy));
    }

    @GetMapping("/recent")
    @Operation(summary = "获取最新资源")
    public ApiResponse<List<ResourceResponse>> getRecent() {
        return ApiResponse.success(resourceService.getRecentResources());
    }

    @GetMapping("/popular")
    @Operation(summary = "获取热门资源（按下载量）")
    public ApiResponse<List<ResourceResponse>> getPopular() {
        return ApiResponse.success(resourceService.getPopularResources());
    }

    @GetMapping("/categories")
    @Operation(summary = "获取所有分类")
    public ApiResponse<List<String>> getCategories() {
        return ApiResponse.success(resourceService.getAllCategories());
    }

    @GetMapping("/stats")
    @Operation(summary = "获取统计信息")
    public ApiResponse<Map<String, Long>> getStats() {
        Map<String, Long> stats = Map.of(
                "total", resourceService.countAll()
        );
        return ApiResponse.success(stats);
    }

    @GetMapping("/search")
    @Operation(summary = "搜索资源")
    public ApiResponse<PageResponse<ResourceResponse>> search(
            @Parameter(description = "搜索关键词") @RequestParam(defaultValue = "") String q,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(resourceService.searchResources(q, page, size));
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "按分类筛选")
    public ApiResponse<PageResponse<ResourceResponse>> getByCategory(
            @PathVariable String category,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(resourceService.getByCategory(category, page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取资源详情（同时增加浏览量）")
    public ApiResponse<ResourceResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(resourceService.getResourceById(id));
    }

    @PostMapping
    @Operation(summary = "提交新资源")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ResourceResponse> create(@Valid @RequestBody ResourceRequest request) {
        return ApiResponse.success("资源发布成功", resourceService.createResource(request));
    }

    @PostMapping("/{id}/rate")
    @Operation(summary = "给资源评分")
    public ApiResponse<ResourceResponse> rate(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "评分请求")
            @RequestBody Map<String, Object> body) {
        Integer score = (Integer) body.get("score");
        Object sessionIdObj = body.get("sessionId");
        String sessionId = sessionIdObj != null ? sessionIdObj.toString() : "default-" + System.currentTimeMillis();
        return ApiResponse.success(resourceService.rateResource(id, score, sessionId));
    }

    @PostMapping("/{id}/download")
    @Operation(summary = "记录下载")
    public ApiResponse<ResourceResponse> download(@PathVariable Long id) {
        return ApiResponse.success(resourceService.incrementDownloads(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除资源（软删除）")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> delete(@PathVariable Long id) {
        resourceService.deleteResource(id);
        return ApiResponse.success("删除成功", null);
    }
}
