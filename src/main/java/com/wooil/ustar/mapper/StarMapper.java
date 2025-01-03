package com.wooil.ustar.mapper;

import com.wooil.ustar.domain.Star;
import com.wooil.ustar.dto.star.StarResDto;
import java.util.List;
import java.util.stream.Collectors;

public class StarMapper {

    public static StarResDto toStarResDto(Star star) {
        return StarResDto.builder()
            .starUid(star.getStarUid())
            .starMessage(star.getStarMessage())
            .starTimeDuration(star.getStarTimeDuration())
            .starTodayDate(star.getStarTodayDate())
            .categoryUid(star.getCategory().getCategoryUid())
            .categoryName(star.getCategory().getCategoryName())
            .starCoordinateX(star.getStarCoordinateX())
            .starCoordinateY(star.getStarCoordinateY())
            .build();
    }

    public static List<StarResDto> toStarResDtoList(List<Star> stars) {
        return stars.stream()
            .map(StarMapper::toStarResDto)
            .collect(Collectors.toList());
    }
}
