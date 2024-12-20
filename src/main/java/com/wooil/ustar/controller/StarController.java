package com.wooil.ustar.controller;

import com.wooil.ustar.Util.userDetails.CustomUserDetails;
import com.wooil.ustar.dto.response.APIResponse;
import com.wooil.ustar.dto.task.CreateStarRequestDto;
import com.wooil.ustar.dto.task.DeleteStarRequestDto;
import com.wooil.ustar.dto.task.StarResDto;
import com.wooil.ustar.dto.task.UpdateStarRequestDto;
import com.wooil.ustar.enums.ErrorCode;
import com.wooil.ustar.exception.CustomException;
import com.wooil.ustar.service.StarService;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/stars")
@RequiredArgsConstructor
public class StarController {

    private final StarService starService;

    @PostMapping("/create")
    public ResponseEntity<APIResponse<StarResDto>> createTask(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody CreateStarRequestDto request) {
        APIResponse<StarResDto> resp;

        try {
            StarResDto task = starService.createStar(userDetails, request);
            resp = new APIResponse<>(true, task);
            return ResponseEntity.ok(resp);
        } catch (CustomException e) {
            final boolean isGlobalError = e.getErrorCode() == ErrorCode.GLOBAL_001
                || e.getErrorCode() == ErrorCode.GLOBAL_002;
            resp = new APIResponse<>(!isGlobalError, e.getErrorCode(), e.getMessage());
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            log.error("Unexpected error while creating task", e);
            resp = new APIResponse<>(false, ErrorCode.GLOBAL_002, e.getMessage());
            return ResponseEntity.ok(resp);
        }
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<StarResDto>>> getAllTasks(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        APIResponse<List<StarResDto>> resp;
        try {
            List<StarResDto> tasks = starService.getAllStarsByUser(userDetails);
            resp = new APIResponse<>(true, tasks);
            return ResponseEntity.ok(resp);
        } catch (CustomException e) {
            final boolean isGlobalError = e.getErrorCode() == ErrorCode.GLOBAL_001
                || e.getErrorCode() == ErrorCode.GLOBAL_002;
            resp = new APIResponse<>(!isGlobalError, e.getErrorCode(), e.getMessage());
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            log.error("Unexpected error while getting all stars", e);
            resp = new APIResponse<>(false, ErrorCode.GLOBAL_002,
                e.getMessage(), Collections.emptyList());
            return ResponseEntity.ok(resp);
        }
    }

    @GetMapping("/date")
    public ResponseEntity<APIResponse<List<StarResDto>>> getTasksByDate(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
    ) {
        APIResponse<List<StarResDto>> resp;
        try {
            List<StarResDto> tasks = starService.getStarsByDate(userDetails, date);
            resp = new APIResponse<>(true, tasks);
            return ResponseEntity.ok(resp);
        } catch (CustomException e) {
            final boolean isGlobalError = e.getErrorCode() == ErrorCode.GLOBAL_001
                || e.getErrorCode() == ErrorCode.GLOBAL_002;
            resp = new APIResponse<>(!isGlobalError, e.getErrorCode(), e.getMessage());
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            log.error("Unexpected error while getting stars by date", e);
            resp = new APIResponse<>(false, ErrorCode.GLOBAL_002,
                e.getMessage(), Collections.emptyList());
            return ResponseEntity.ok(resp);
        }
    }


    @PatchMapping("/update")
    public ResponseEntity<APIResponse<StarResDto>> updateTask(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody UpdateStarRequestDto request
    ) {
        APIResponse<StarResDto> resp;
        try {
            StarResDto task = starService.updateStar(userDetails, request);
            resp = new APIResponse<>(true, task);
            return ResponseEntity.ok(resp);
        } catch (CustomException e) {
            final boolean isGlobalError = e.getErrorCode() == ErrorCode.GLOBAL_001
                || e.getErrorCode() == ErrorCode.GLOBAL_002;
            resp = new APIResponse<>(!isGlobalError, e.getErrorCode(), e.getMessage());
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            log.error("Unexpected error while updating task", e);
            resp = new APIResponse<>(false, ErrorCode.GLOBAL_002, e.getMessage());
            return ResponseEntity.ok(resp);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<APIResponse<Void>> deleteTask(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody DeleteStarRequestDto request
    ) {
        APIResponse<Void> resp;
        try {
            starService.deleteStar(userDetails, request);
            resp = new APIResponse<>(true);
            return ResponseEntity.ok(resp);
        } catch (CustomException e) {
            final boolean isGlobalError = e.getErrorCode() == ErrorCode.GLOBAL_001
                || e.getErrorCode() == ErrorCode.GLOBAL_002;
            resp = new APIResponse<>(!isGlobalError, e.getErrorCode(), e.getMessage());
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            log.error("Unexpected error while deleting task", e);
            resp = new APIResponse<>(false, ErrorCode.GLOBAL_002, e.getMessage());
            return ResponseEntity.ok(resp);
        }
    }
}
