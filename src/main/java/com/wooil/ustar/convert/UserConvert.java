package com.wooil.ustar.convert;


import com.wooil.ustar.domain.User;
import com.wooil.ustar.dto.user.GetUserDto;
import com.wooil.ustar.enums.ErrorCode;
import com.wooil.ustar.exception.CustomException;

public class UserConvert {
    public static GetUserDto user2GetUserDto(User user) {
        try{
            return GetUserDto.builder()
            .userUid(user.getUserUid())
            .userName(user.getUserName())
            .userEmail(user.getUserEmail())
            .build();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.unknown, e.getMessage());
        }

    }
}
