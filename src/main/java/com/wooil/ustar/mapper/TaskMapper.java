package com.wooil.ustar.mapper;

import com.wooil.ustar.domain.Task;
import com.wooil.ustar.dto.task.TaskResDto;
import java.util.List;
import java.util.stream.Collectors;

public class TaskMapper {

    public static TaskResDto toTaskResDto(Task task) {
        return TaskResDto.builder()
            .taskUid(task.getTaskUid())
            .taskMessage(task.getTaskMessage())
            .taskTimeDuration(task.getTaskTimeDuration())
            .taskTodayDate(task.getTaskTodayDate())
            .categoryUid(task.getCategory().getCategoryUid())
            .categoryName(task.getCategory().getCategoryName())
            .build();
    }

    public static List<TaskResDto> toTaskResDtoList(List<Task> tasks) {
        return tasks.stream()
            .map(TaskMapper::toTaskResDto)
            .collect(Collectors.toList());
    }
}
