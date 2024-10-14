package com.wooil.ustar.mapper;


import com.wooil.ustar.domain.User;
import com.wooil.ustar.dto.user.GetUserDto;
import com.wooil.ustar.dto.user.UpdateUserResDto;
import com.wooil.ustar.enums.ErrorCode;
import com.wooil.ustar.exception.CustomException;

public class UserMapper {

    public static GetUserDto user2GetUserDto(User user) {
        try {
            return GetUserDto.builder()
                .userUid(user.getUserUid())
                .userName(user.getUserName())
                .userEmail(user.getUserEmail())
                .build();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.GLOBAL_001, e.getMessage());
        }

    }

    public static UpdateUserResDto user2UpdateUserResDto(User user) {
        try {
            return UpdateUserResDto.builder()
                .userUid(user.getUserUid())
                .userName(user.getUserName())
                .userEmail(user.getUserEmail())
                .build();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.GLOBAL_001, e.getMessage());
        }
    }
}
