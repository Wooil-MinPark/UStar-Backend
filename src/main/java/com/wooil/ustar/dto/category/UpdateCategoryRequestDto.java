package com.wooil.ustar.dto.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateCategoryRequestDto {
    private Long categoryUid;
    private String categoryName;
    private String categoryColor;
}
