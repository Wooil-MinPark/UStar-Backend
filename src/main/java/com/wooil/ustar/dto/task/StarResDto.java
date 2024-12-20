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
public class StarResDto {

    private Long starUid;
    private String starMessage;
    private Integer starTimeDuration;
    private LocalDate starTodayDate;
    private Long categoryUid;
    private String categoryName;

}
