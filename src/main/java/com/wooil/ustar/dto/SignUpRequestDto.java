package com.wooil.ustar.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDto {
    @NotBlank(message = "Name is required")
    private String userName;

    @NotBlank(message = "Email is required")
    private String userEmail;

    // 이렇게 사이즈 지정 가능
    // @Size(min = 8, message = "Password must be at least 8 characters long")
    @NotBlank(message = "Password is required")
    private String userPassword;
}
