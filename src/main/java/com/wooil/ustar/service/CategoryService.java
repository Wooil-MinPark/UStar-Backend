package com.wooil.ustar.service;

import com.wooil.ustar.Util.userDetails.CustomUserDetails;
import com.wooil.ustar.domain.Category;
import com.wooil.ustar.domain.User;
import com.wooil.ustar.dto.category.CreateCategoryRequestDto;
import com.wooil.ustar.dto.category.DeleteCategoryRequestDto;
import com.wooil.ustar.dto.category.GetCategoryRequestDto;
import com.wooil.ustar.dto.category.UpdateCategoryRequestDto;
import com.wooil.ustar.enums.ErrorCode;
import com.wooil.ustar.exception.CustomException;
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

    public Category createCategory(CustomUserDetails userDetails,
        CreateCategoryRequestDto request) {
        try {
            User user = userRepository.findByUserEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_004));

            Category category = Category.builder()
                .categoryName(request.getCategoryName())
                .categoryColor(request.getCategoryColor())
                .build();

            user.addCategory(category);
            return categoryRepository.save(category);

        } catch (CustomException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.GLOBAL_001, e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Set<Category> getAllCategoriesByUser(CustomUserDetails userDetails) {
        try{
            return categoryRepository.findCategoriesByUserEmail(userDetails.getUsername());
        }catch (CustomException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.GLOBAL_001, e.getMessage());
        }
    }

    public Category getCategoryById(GetCategoryRequestDto request) {
        try{
            return categoryRepository.findByCategoryUid(
                    request.getCategoryUid())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_001));
        }catch (CustomException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.GLOBAL_001, e.getMessage());
        }
    }



    public Category updateCategory(CustomUserDetails userDetails,
        UpdateCategoryRequestDto request) {
        try {
            Category category = categoryRepository.findByCategoryUid(
                    request.getCategoryUid())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_001));

            category.setCategoryName(request.getCategoryName());
            category.setCategoryColor(request.getCategoryColor());
            return categoryRepository.save(category);
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
