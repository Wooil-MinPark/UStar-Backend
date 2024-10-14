package com.wooil.ustar.service;

import com.wooil.ustar.Util.userDetails.CustomUserDetails;
import com.wooil.ustar.domain.Category;
import com.wooil.ustar.domain.User;
import com.wooil.ustar.dto.category.CategoryResDto;
import com.wooil.ustar.dto.category.CreateCategoryRequestDto;
import com.wooil.ustar.dto.category.DeleteCategoryRequestDto;
import com.wooil.ustar.dto.category.UpdateCategoryRequestDto;
import com.wooil.ustar.enums.ErrorCode;
import com.wooil.ustar.exception.CustomException;
import com.wooil.ustar.mapper.CategoryMapper;
import com.wooil.ustar.repository.CategoryRepository;
import com.wooil.ustar.repository.UserRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryResDto createCategory(CustomUserDetails userDetails,
        CreateCategoryRequestDto request) {
        try {
            User user = userRepository.findByUserEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_004));

            Category category = Category.builder()
                .categoryName(request.getCategoryName())
                .categoryColor(request.getCategoryColor())
                .build();

            user.addCategory(category);
            Category savedCategory = categoryRepository.save(category);
            return CategoryMapper.toCategoryResDto(savedCategory);

        } catch (CustomException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.GLOBAL_001, e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Set<CategoryResDto> getAllCategoriesByUser(CustomUserDetails userDetails) {
        try {
            Set<Category> categories = categoryRepository.findCategoriesByUserEmail(
                userDetails.getUsername());
            return CategoryMapper.toCategoryResDtoSet(categories);
        } catch (CustomException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.GLOBAL_001, e.getMessage());
        }
    }

    public CategoryResDto getCategoryById(CustomUserDetails userDetails, Long categoryUid) {
        try {
            User user = userRepository.findByUserEmail(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_004));

            Category category = categoryRepository.findByCategoryUid(
                    categoryUid)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_001));

            if (!category.getUser().getUserUid().equals(user.getUserUid())) {
                throw new CustomException(ErrorCode.CATEGORY_002);
            }

            return CategoryMapper.toCategoryResDto(category);

        } catch (CustomException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.GLOBAL_001, e.getMessage());
        }
    }


    public CategoryResDto updateCategory(CustomUserDetails userDetails,
        UpdateCategoryRequestDto request) {
        try {
            User user = userRepository.findByUserEmail(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_004));

            Category category = categoryRepository.findByCategoryUid(
                    request.getCategoryUid())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_001));

            if (!category.getUser().getUserUid().equals(user.getUserUid())) {
                throw new CustomException(ErrorCode.CATEGORY_002);
            }

            if (request.getCategoryName() != null) {
                category.setCategoryName(request.getCategoryName());
            }

            if (request.getCategoryColor() != null) {
                category.setCategoryColor(request.getCategoryColor());
            }

            Category savedCategory = categoryRepository.save(category);
            return CategoryMapper.toCategoryResDto(savedCategory);
        } catch (CustomException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.GLOBAL_001, e.getMessage());
        }
    }

    public void deleteCategory(CustomUserDetails userDetails,
        DeleteCategoryRequestDto request) {
        try {
            User user = userRepository.findByUserEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_004));

            Category category = categoryRepository.findByCategoryUid(
                    request.getCategoryUid())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_001));
            user.removeCategory(category);
            categoryRepository.delete(category);
        } catch (CustomException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.GLOBAL_001, e.getMessage());
        }

    }

//    @Transactional(readOnly = true)
//    public Set<Category> getAllCategoriesByUser() {}
}
