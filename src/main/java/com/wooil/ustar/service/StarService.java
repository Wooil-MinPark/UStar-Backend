package com.wooil.ustar.service;


import com.wooil.ustar.Util.userDetails.CustomUserDetails;
import com.wooil.ustar.domain.Category;
import com.wooil.ustar.domain.Star;
import com.wooil.ustar.domain.User;
import com.wooil.ustar.dto.task.CreateStarRequestDto;
import com.wooil.ustar.dto.task.DeleteStarRequestDto;
import com.wooil.ustar.dto.task.StarResDto;
import com.wooil.ustar.dto.task.UpdateStarRequestDto;
import com.wooil.ustar.enums.ErrorCode;
import com.wooil.ustar.exception.CustomException;
import com.wooil.ustar.mapper.StarMapper;
import com.wooil.ustar.repository.CategoryRepository;
import com.wooil.ustar.repository.StarRepository;
import com.wooil.ustar.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class StarService {

    private final StarRepository starRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;


    public StarResDto createStar(CustomUserDetails userDetails, CreateStarRequestDto request) {
        try {
            /// user 유효성 검사
            User user = userRepository.findByUserEmail(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_004));

            /// category 유효성 검사
            Category category = categoryRepository.findByCategoryUid(request.getCategoryUid())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_001));

            /// user와 category 권한 검사
            if (!category.getUser().getUserUid().equals(user.getUserUid())) {
                throw new CustomException(ErrorCode.CATEGORY_002);
            }

            /// request를 통해 star 생성
            Star star = Star.builder()
                .starMessage(request.getStarMessage())
                .starTimeDuration(request.getStarTimeDuration())
                .starTodayDate(request.getStarTodayDate())
                .build();

            /// star category에 추가
            category.addStar(star);

            Star savedStar = starRepository.save(star);
            return StarMapper.toStarResDto(savedStar);
        } catch (CustomException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.GLOBAL_001, e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<StarResDto> getStarsByDate(CustomUserDetails userDetails, LocalDate date) {
        try {
            User user = userRepository.findByUserEmail(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_004));

            List<Star> stars = starRepository.findByTodayDateWithCategory(date);
            stars = stars.stream()
                .filter(star -> star.getCategory().getUser().getUserUid().equals(user.getUserUid()))
                .toList();

            return StarMapper.toStarResDtoList(stars);
        } catch (CustomException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.GLOBAL_001, e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<StarResDto> getAllStarsByUser(CustomUserDetails userDetails) {
        try {
            User user = userRepository.findByUserEmail(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_004));

            List<Star> stars = starRepository.findAllByUserUid(user.getUserUid());
            return StarMapper.toStarResDtoList(stars);
        } catch (CustomException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.GLOBAL_001, e.getMessage());
        }
    }

    public StarResDto updateStar(CustomUserDetails userDetails, UpdateStarRequestDto request) {
        try {
            /// user 존재유무검사
            User user = userRepository.findByUserEmail(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_004));

            /// star 존재유무 검사
            Star star = starRepository.findById(request.getStarUid())
                .orElseThrow(() -> new CustomException(ErrorCode.STAR_001));

            /// star 권한 유효성 검사
            if (!star.getCategory().getUser().getUserUid().equals(user.getUserUid())) {
                throw new CustomException(ErrorCode.STAR_002);
            }

            /// 메시지변경
            if (request.getStarMessage() != null) {
                star.setStarMessage(request.getStarMessage());
            }
            /// time duration 변경 (아마 안쓸듯?)
            if (request.getStarTimeDuration() != null) {
                star.setStarTimeDuration(request.getStarTimeDuration());
            }
            /// today 변경 (안쓸듯?)
            if (request.getStarTodayDate() != null) {
                star.setStarTodayDate(request.getStarTodayDate());
            }

            // 카테고리 변경이 요청된 경우
            if (request.getCategoryUid() != null &&
                !star.getCategory().getCategoryUid().equals(request.getCategoryUid())) {
                Category newCategory = categoryRepository.findByCategoryUid(
                        request.getCategoryUid())
                    .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_001));

                if (!newCategory.getUser().getUserUid().equals(user.getUserUid())) {
                    throw new CustomException(ErrorCode.CATEGORY_002);
                }

                star.getCategory().removeStar(star);
                newCategory.addStar(star);
            }

            Star savedStar = starRepository.save(star);
            return StarMapper.toStarResDto(savedStar);
        } catch (CustomException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.GLOBAL_001, e.getMessage());
        }
    }

    public void deleteStar(CustomUserDetails userDetails, DeleteStarRequestDto request) {
        try {

            User user = userRepository.findByUserEmail(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_004));

            Star star = starRepository.findById(request.getStarUid())
                .orElseThrow(() -> new CustomException(ErrorCode.STAR_001));

            if (!star.getCategory().getUser().getUserUid().equals(user.getUserUid())) {
                throw new CustomException(ErrorCode.STAR_002);
            }

            star.getCategory().removeStar(star);
            starRepository.delete(star);
        } catch (CustomException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.GLOBAL_001, e.getMessage());
        }
    }
}
