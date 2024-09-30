package com.wooil.ustar.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetUserDto {
    private long userUid;
    private String userEmail;
    private String userName;

}
