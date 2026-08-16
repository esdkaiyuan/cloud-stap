package com.disk.share.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceResponse {
    private Long id;
    private String title;
    private String description;
    private String url;
    private String category;
    private String sourceType;
    private String extractCode;
    private Integer views;
    private Integer downloads;
    private Double avgRating;
    private Integer ratingCount;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
