package com.wooil.ustar.mapper;

import com.wooil.ustar.domain.Category;
import com.wooil.ustar.dto.category.CategoryResDto;
import com.wooil.ustar.enums.ErrorCode;
import com.wooil.ustar.exception.CustomException;
import java.util.Set;
import java.util.stream.Collectors;

public class CategoryMapper {
    public static CategoryResDto toCategoryResDto(Category category) {
        try{
            return CategoryResDto.builder()
                .categoryUid(category.getCategoryUid())
                .categoryName(category.getCategoryName())
                .categoryColor(category.getCategoryColor())
                .build();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.GLOBAL_001, e.getMessage());
        }
    }

    public static Set<CategoryResDto> toCategoryResDtoSet(Set<Category> categories) {
        try{
            return categories.stream()
                .map(CategoryMapper::toCategoryResDto)
                .collect(Collectors.toSet());
        } catch (Exception e) {
            throw new CustomException(ErrorCode.GLOBAL_001, e.getMessage());
        }
    }
}
