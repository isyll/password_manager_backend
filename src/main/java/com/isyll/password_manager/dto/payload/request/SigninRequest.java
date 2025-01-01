package com.isyll.password_manager.dto.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SigninRequest {

    @NotBlank(message = "{validation.user.email_cannot_be_empty}")
    @NotNull(message = "{validation.user.email_is_mandatory}")
    private String email;

    @NotBlank(message = "{validation.user.password_cannot_be_empty}")
    @NotNull(message = "{validation.user.username_is_mandatory}")
    private String password;
}
