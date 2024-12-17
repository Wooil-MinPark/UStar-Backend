package com.wooil.ustar.service;


import com.wooil.ustar.Util.userDetails.CustomUserDetails;
import com.wooil.ustar.domain.Category;
import com.wooil.ustar.domain.Task;
import com.wooil.ustar.domain.User;
import com.wooil.ustar.dto.task.CreateTaskRequestDto;
import com.wooil.ustar.dto.task.DeleteTaskRequestDto;
import com.wooil.ustar.dto.task.TaskResDto;
import com.wooil.ustar.dto.task.UpdateTaskRequestDto;
import com.wooil.ustar.enums.ErrorCode;
import com.wooil.ustar.exception.CustomException;
import com.wooil.ustar.mapper.TaskMapper;
import com.wooil.ustar.repository.CategoryRepository;
import com.wooil.ustar.repository.TaskRepository;
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
public class TaskService {

    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;


    public TaskResDto createTask(CustomUserDetails userDetails, CreateTaskRequestDto request) {
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

            /// request를 통해 task 생성
            Task task = Task.builder()
                .taskMessage(request.getTaskMessage())
                .taskTimeDuration(request.getTaskTimeDuration())
                .taskTodayDate(request.getTaskTodayDate())
                .build();

            /// task category에 추가
            category.addTask(task);

            Task savedTask = taskRepository.save(task);
            return TaskMapper.toTaskResDto(savedTask);
        } catch (CustomException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.GLOBAL_001, e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<TaskResDto> getTasksByDate(CustomUserDetails userDetails, LocalDate date) {
        try {
            User user = userRepository.findByUserEmail(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_004));

            List<Task> tasks = taskRepository.findByTodayDateWithCategory(date);
            tasks = tasks.stream()
                .filter(task -> task.getCategory().getUser().getUserUid().equals(user.getUserUid()))
                .toList();

            return TaskMapper.toTaskResDtoList(tasks);
        } catch (CustomException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.GLOBAL_001, e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<TaskResDto> getAllTasksByUser(CustomUserDetails userDetails) {
        try {
            User user = userRepository.findByUserEmail(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_004));

            List<Task> tasks = taskRepository.findAllByUserUid(user.getUserUid());
            return TaskMapper.toTaskResDtoList(tasks);
        } catch (CustomException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.GLOBAL_001, e.getMessage());
        }
    }

    public TaskResDto updateTask(CustomUserDetails userDetails, UpdateTaskRequestDto request) {
        try {
            /// user 존재유무검사
            User user = userRepository.findByUserEmail(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_004));

            /// task 존재유무 검사
            Task task = taskRepository.findById(request.getTaskUid())
                .orElseThrow(() -> new CustomException(ErrorCode.TASK_001));

            /// task 권한 유효성 검사
            if (!task.getCategory().getUser().getUserUid().equals(user.getUserUid())) {
                throw new CustomException(ErrorCode.TASK_002);
            }

            /// 메시지변경
            if (request.getTaskMessage() != null) {
                task.setTaskMessage(request.getTaskMessage());
            }
            /// time duration 변경 (아마 안쓸듯?)
            if (request.getTaskTimeDuration() != null) {
                task.setTaskTimeDuration(request.getTaskTimeDuration());
            }
            /// today 변경 (안쓸듯?)
            if (request.getTaskTodayDate() != null) {
                task.setTaskTodayDate(request.getTaskTodayDate());
            }

            // 카테고리 변경이 요청된 경우
            if (request.getCategoryUid() != null &&
                !task.getCategory().getCategoryUid().equals(request.getCategoryUid())) {
                Category newCategory = categoryRepository.findByCategoryUid(
                        request.getCategoryUid())
                    .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_001));

                if (!newCategory.getUser().getUserUid().equals(user.getUserUid())) {
                    throw new CustomException(ErrorCode.CATEGORY_002);
                }

                task.getCategory().removeTask(task);
                newCategory.addTask(task);
            }

            Task savedTask = taskRepository.save(task);
            return TaskMapper.toTaskResDto(savedTask);
        } catch (CustomException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.GLOBAL_001, e.getMessage());
        }
    }

    public void deleteTask(CustomUserDetails userDetails, DeleteTaskRequestDto request) {
        try {

            User user = userRepository.findByUserEmail(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_004));

            Task task = taskRepository.findById(request.getTaskUid())
                .orElseThrow(() -> new CustomException(ErrorCode.TASK_001));

            if (!task.getCategory().getUser().getUserUid().equals(user.getUserUid())) {
                throw new CustomException(ErrorCode.TASK_002);
            }

            task.getCategory().removeTask(task);
            taskRepository.delete(task);
        }catch (CustomException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.GLOBAL_001, e.getMessage());
        }
    }
}
