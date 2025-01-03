package com.wooil.ustar.dto.star;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStarRequestDto {

    private Long starUid;
    private String starMessage;
    private Integer starTimeDuration;
    private LocalDate starTodayDate;
    private Long categoryUid;
    private Integer starCoordinateX;
    private Integer starCoordinateY;
}
