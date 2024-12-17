package com.wooil.ustar.controller;

import com.wooil.ustar.Util.userDetails.CustomUserDetails;
import com.wooil.ustar.dto.response.APIResponse;
import com.wooil.ustar.dto.task.CreateTaskRequestDto;
import com.wooil.ustar.dto.task.DeleteTaskRequestDto;
import com.wooil.ustar.dto.task.TaskResDto;
import com.wooil.ustar.dto.task.UpdateTaskRequestDto;
import com.wooil.ustar.enums.ErrorCode;
import com.wooil.ustar.exception.CustomException;
import com.wooil.ustar.service.TaskService;
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
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/create")
    public ResponseEntity<APIResponse<TaskResDto>> createTask(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody CreateTaskRequestDto request) {
        APIResponse<TaskResDto> resp;

        try {
            TaskResDto task = taskService.createTask(userDetails, request);
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
    public ResponseEntity<APIResponse<List<TaskResDto>>> getAllTasks(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        APIResponse<List<TaskResDto>> resp;
        try {
            List<TaskResDto> tasks = taskService.getAllTasksByUser(userDetails);
            resp = new APIResponse<>(true, tasks);
            return ResponseEntity.ok(resp);
        } catch (CustomException e) {
            final boolean isGlobalError = e.getErrorCode() == ErrorCode.GLOBAL_001 || e.getErrorCode() == ErrorCode.GLOBAL_002;
            resp = new APIResponse<>(!isGlobalError, e.getErrorCode(), e.getMessage());
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            log.error("Unexpected error while getting all tasks", e);
            resp = new APIResponse<>(false, ErrorCode.GLOBAL_002,
                e.getMessage(), Collections.emptyList());
            return ResponseEntity.ok(resp);
        }
    }

    @GetMapping("/date")
    public ResponseEntity<APIResponse<List<TaskResDto>>> getTasksByDate(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
    ) {
        APIResponse<List<TaskResDto>> resp;
        try {
            List<TaskResDto> tasks = taskService.getTasksByDate(userDetails, date);
            resp = new APIResponse<>(true, tasks);
            return ResponseEntity.ok(resp);
        } catch (CustomException e) {
            final boolean isGlobalError = e.getErrorCode() == ErrorCode.GLOBAL_001 || e.getErrorCode() == ErrorCode.GLOBAL_002;
            resp = new APIResponse<>(!isGlobalError, e.getErrorCode(), e.getMessage());
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            log.error("Unexpected error while getting tasks by date", e);
            resp = new APIResponse<>(false, ErrorCode.GLOBAL_002,
                e.getMessage(), Collections.emptyList());
            return ResponseEntity.ok(resp);
        }
    }


    @PatchMapping("/update")
    public ResponseEntity<APIResponse<TaskResDto>> updateTask(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody UpdateTaskRequestDto request
    ) {
        APIResponse<TaskResDto> resp;
        try {
            TaskResDto task = taskService.updateTask(userDetails, request);
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
        @RequestBody DeleteTaskRequestDto request
    ) {
        APIResponse<Void> resp;
        try {
            taskService.deleteTask(userDetails, request);
            resp = new APIResponse<>(true);
            return ResponseEntity.ok(resp);
        } catch (CustomException e) {
            final boolean isGlobalError = e.getErrorCode() == ErrorCode.GLOBAL_001 || e.getErrorCode() == ErrorCode.GLOBAL_002;
            resp = new APIResponse<>(!isGlobalError, e.getErrorCode(), e.getMessage());
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            log.error("Unexpected error while deleting task", e);
            resp = new APIResponse<>(false, ErrorCode.GLOBAL_002, e.getMessage());
            return ResponseEntity.ok(resp);
        }
    }
}
