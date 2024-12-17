package com.wooil.ustar.dto.task;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequestDto {

    private String taskMessage;
    private Integer taskTimeDuration;
    private LocalDate taskTodayDate;
    private Long categoryUid;
}
