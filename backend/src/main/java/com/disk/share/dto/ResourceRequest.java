package com.disk.share.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResourceRequest {

    @NotBlank(message = "资源标题不能为空")
    @Size(max = 200, message = "标题长度不能超过200个字符")
    private String title;

    @NotBlank(message = "资源描述不能为空")
    @Size(max = 1000, message = "描述长度不能超过1000个字符")
    private String description;

    @NotBlank(message = "资源链接不能为空")
    @Size(max = 1000, message = "链接长度不能超过1000个字符")
    private String url;

    @Size(max = 100, message = "分类长度不能超过100个字符")
    private String category;

    @Size(max = 50, message = "来源类型长度不能超过50个字符")
    private String sourceType;

    @Size(max = 50, message = "提取码长度不能超过50个字符")
    private String extractCode;
}
