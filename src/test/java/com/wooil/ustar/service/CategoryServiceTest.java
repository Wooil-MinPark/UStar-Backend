package com.wooil.ustar.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wooil.ustar.Util.userDetails.CustomUserDetails;
import com.wooil.ustar.domain.Category;
import com.wooil.ustar.domain.User;
import com.wooil.ustar.dto.category.CategoryResDto;
import com.wooil.ustar.dto.category.CreateCategoryRequestDto;
import com.wooil.ustar.dto.category.DeleteCategoryRequestDto;
import com.wooil.ustar.dto.category.UpdateCategoryRequestDto;
import com.wooil.ustar.enums.ErrorCode;
import com.wooil.ustar.exception.CustomException;
import com.wooil.ustar.repository.CategoryRepository;
import com.wooil.ustar.repository.UserRepository;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CategoryService categoryService;

    private User user;
    private Category category;
    private CustomUserDetails userDetails;
    private CreateCategoryRequestDto createRequest;
    private UpdateCategoryRequestDto updateRequest;
    private DeleteCategoryRequestDto deleteRequest;

    @BeforeEach
    void setup() {
        final String name = "testName";
        final String email = "test@test.com";
        final String password = "password";
        final String encodedPassword = "encodedPassword";
        final Long uid = 1L;

        final String categoryName = "testCategory";
        final String categoryColor = "#FF0000";
        final String updateCategoryName = "testUpdateCategory";
        final String updateCategoryColor = "#0000FF";

        user = User.builder()
            .userUid(uid)
            .userName(name)
            .userEmail(email)
            .userPassword(encodedPassword)
            .build();

        category = Category.builder()
            .categoryUid(uid)
            .categoryName(categoryName)
            .categoryColor(categoryColor)
            .user(user)
            .build();

        userDetails = new CustomUserDetails(user);
        createRequest = new CreateCategoryRequestDto(categoryName, categoryColor);
        updateRequest = new UpdateCategoryRequestDto(uid, updateCategoryName, updateCategoryColor);
        deleteRequest = new DeleteCategoryRequestDto(uid);
    }

    @Test
    void createCategory_Success() {
        when(userRepository.findByUserEmail(anyString())).thenReturn(Optional.of(user));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryResDto result = categoryService.createCategory(userDetails, createRequest);

        assertNotNull(result);
        assertEquals(category.getCategoryName(), result.getCategoryName());
        assertEquals(category.getCategoryColor(), result.getCategoryColor());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void createCategory_UserNotFound() {
        when(userRepository.findByUserEmail(anyString())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class,
            () -> categoryService.createCategory(userDetails, createRequest));

        assertEquals(ErrorCode.USER_004, exception.getErrorCode());
    }

    @Test
    void getAllCategoriesByUser_Success() {
        Set<Category> categories = new HashSet<>();
        categories.add(category);
        when(categoryRepository.findCategoriesByUserEmail(anyString())).thenReturn(categories);

        Set<CategoryResDto> result = categoryService.getAllCategoriesByUser(userDetails);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        CategoryResDto categoryResDto = result.iterator().next();
        assertEquals(category.getCategoryName(), categoryResDto.getCategoryName());
    }

    @Test
    void getCategoryById_Success() {
        when(userRepository.findByUserEmail(anyString())).thenReturn(Optional.of(user));
        when(categoryRepository.findByCategoryUid(anyLong())).thenReturn(Optional.of(category));

        CategoryResDto result = categoryService.getCategoryById(userDetails, 1L);

        assertNotNull(result);
        assertEquals(category.getCategoryName(), result.getCategoryName());
        assertEquals(category.getCategoryColor(), result.getCategoryColor());
    }

    @Test
    void getCategoryById_CategoryNotFound() {
        when(userRepository.findByUserEmail(anyString())).thenReturn(Optional.of(user));
        when(categoryRepository.findByCategoryUid(anyLong())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class,
            () -> categoryService.getCategoryById(userDetails, 1L));

        assertEquals(ErrorCode.CATEGORY_001, exception.getErrorCode());
    }

    @Test
    void getCategoryById_UnauthorizedAccess() {
        User otherUser = User.builder()
            .userUid(2L)
            .userName("otherUser")
            .userEmail("other@test.com")
            .build();
        Category otherCategory = Category.builder()
            .categoryUid(1L)
            .categoryName("testCategory")
            .categoryColor("#FF0000")
            .user(otherUser)
            .build();

        when(userRepository.findByUserEmail(anyString())).thenReturn(Optional.of(user));
        when(categoryRepository.findByCategoryUid(anyLong())).thenReturn(
            Optional.of(otherCategory));

        CustomException exception = assertThrows(CustomException.class,
            () -> categoryService.getCategoryById(userDetails, 1L));

        assertEquals(ErrorCode.CATEGORY_002, exception.getErrorCode());
    }

    @Test
    void updateCategory_Success() {
        when(userRepository.findByUserEmail(anyString())).thenReturn(Optional.of(user));
        when(categoryRepository.findByCategoryUid(anyLong())).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryResDto result = categoryService.updateCategory(userDetails, updateRequest);

        assertNotNull(result);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void updateCategory_CategoryNotFound() {
        when(userRepository.findByUserEmail(anyString())).thenReturn(Optional.of(user));
        when(categoryRepository.findByCategoryUid(anyLong())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class,
            () -> categoryService.updateCategory(userDetails, updateRequest));

        assertEquals(ErrorCode.CATEGORY_001, exception.getErrorCode());
    }

    @Test
    void deleteCategory_Success() {
        when(userRepository.findByUserEmail(anyString())).thenReturn(Optional.of(user));
        when(categoryRepository.findByCategoryUid(anyLong())).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).delete(any(Category.class));

        assertDoesNotThrow(() -> categoryService.deleteCategory(userDetails, deleteRequest));
        verify(categoryRepository).delete(any(Category.class));
    }

    @Test
    void deleteCategory_CategoryNotFound() {
        when(userRepository.findByUserEmail(anyString())).thenReturn(Optional.of(user));
        when(categoryRepository.findByCategoryUid(anyLong())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class,
            () -> categoryService.deleteCategory(userDetails, deleteRequest));

        assertEquals(ErrorCode.CATEGORY_001, exception.getErrorCode());
    }
}
