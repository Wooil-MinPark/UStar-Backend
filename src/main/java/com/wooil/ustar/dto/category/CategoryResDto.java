package com.wooil.ustar.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResDto {
    private Long categoryUid;
    private String categoryName;
    private String categoryColor;
}
