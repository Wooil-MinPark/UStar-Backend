package com.wooil.ustar.controller;

import com.wooil.ustar.Util.userDetails.CustomUserDetails;
import com.wooil.ustar.dto.category.CategoryResDto;
import com.wooil.ustar.dto.category.CreateCategoryRequestDto;
import com.wooil.ustar.dto.category.DeleteCategoryRequestDto;
import com.wooil.ustar.dto.category.UpdateCategoryRequestDto;
import com.wooil.ustar.dto.response.APIResponse;
import com.wooil.ustar.enums.ErrorCode;
import com.wooil.ustar.exception.CustomException;
import com.wooil.ustar.service.CategoryService;
import java.util.Collections;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<APIResponse<CategoryResDto>> createCategory(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody CreateCategoryRequestDto request
    ) {
        APIResponse<CategoryResDto> resp;
        try {
            CategoryResDto category = categoryService.createCategory(userDetails, request);
            resp = new APIResponse<>(true, category);
            return ResponseEntity.ok(resp);
        } catch (CustomException e) {
             final boolean isGlobalError = e.getErrorCode() == ErrorCode.GLOBAL_001 || e.getErrorCode() == ErrorCode.GLOBAL_002;
            resp = new APIResponse<>(!isGlobalError, e.getErrorCode(), e.getMessage());
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            log.error("Unexpected error while create category", e);
            resp = new APIResponse<>(false, ErrorCode.GLOBAL_002,
                e.getMessage());
            return ResponseEntity.ok(resp);
        }
    }

    @GetMapping
    public ResponseEntity<APIResponse<Set<CategoryResDto>>> getAllCategories(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        APIResponse<Set<CategoryResDto>> resp;
        try {
            Set<CategoryResDto> categories = categoryService.getAllCategoriesByUser(userDetails);

            resp = new APIResponse<>(true, categories);
            return ResponseEntity.ok(resp);
        } catch (CustomException e) {
             final boolean isGlobalError = e.getErrorCode() == ErrorCode.GLOBAL_001 || e.getErrorCode() == ErrorCode.GLOBAL_002;
            resp = new APIResponse<>(!isGlobalError, e.getErrorCode(), e.getMessage());
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            log.error("Unexpected error while get all categories", e);
            resp = new APIResponse<>(false, ErrorCode.GLOBAL_002,
                e.getMessage(), Collections.emptySet());
            return ResponseEntity.ok(resp);
        }
    }

    @GetMapping("/{categoryUid}")
    public ResponseEntity<APIResponse<CategoryResDto>> getCategory(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long categoryUid
    ) {
        APIResponse<CategoryResDto> resp;
        try {
            CategoryResDto category = categoryService.getCategoryById(userDetails, categoryUid);
            resp = new APIResponse<>(true, category);
            return ResponseEntity.ok(resp);
        } catch (CustomException e) {
             final boolean isGlobalError = e.getErrorCode() == ErrorCode.GLOBAL_001 || e.getErrorCode() == ErrorCode.GLOBAL_002;
            resp = new APIResponse<>(!isGlobalError, e.getErrorCode(), e.getMessage());
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            log.error("Unexpected error while get category", e);
            resp = new APIResponse<>(false, ErrorCode.GLOBAL_002,
                e.getMessage());
            return ResponseEntity.ok(resp);
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<APIResponse<CategoryResDto>> updateCategory(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody UpdateCategoryRequestDto request
    ){
        APIResponse<CategoryResDto> resp;
        try {
            CategoryResDto category = categoryService.updateCategory(userDetails, request);
            resp = new APIResponse<>(true, category);
            return ResponseEntity.ok(resp);
        } catch (CustomException e) {
             final boolean isGlobalError = e.getErrorCode() == ErrorCode.GLOBAL_001 || e.getErrorCode() == ErrorCode.GLOBAL_002;
            resp = new APIResponse<>(!isGlobalError, e.getErrorCode(), e.getMessage());
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            log.error("Unexpected error while update category", e);
            resp = new APIResponse<>(false, ErrorCode.GLOBAL_002,
                e.getMessage());
            return ResponseEntity.ok(resp);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<APIResponse<Void>> deleteCategory(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody DeleteCategoryRequestDto request
    ){
        APIResponse<Void> resp;
        try {
            categoryService.deleteCategory(userDetails, request);
            resp = new APIResponse<>(true);
            return ResponseEntity.ok(resp);
        } catch (CustomException e) {
             final boolean isGlobalError = e.getErrorCode() == ErrorCode.GLOBAL_001 || e.getErrorCode() == ErrorCode.GLOBAL_002;
            resp = new APIResponse<>(!isGlobalError, e.getErrorCode(), e.getMessage());
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            log.error("Unexpected error while update category", e);
            resp = new APIResponse<>(false, ErrorCode.GLOBAL_002,
                e.getMessage());
            return ResponseEntity.ok(resp);
        }
    }
}
