package com.isyll.password_manager.dto.payload.request;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.isyll.password_manager.validators.CountryValidation;
import com.isyll.password_manager.validators.DateValidation;
import com.isyll.password_manager.validators.E164PhoneValidation;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignUpRequest {

    @NotNull(message = "{validation.user.birthdate_is_mandatory}")
    @NotBlank(message = "{validation.user.birthdate_is_mandatory}")
    @DateValidation(message = "{validation.user.birthdate_is_invalid}")
    @JsonProperty("date_of_birth")
    private String dateOfBirth;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[A-Za-z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$", message = "{validation.user.password_is_invalid}")
    @NotBlank(message = "{validation.user.password_cannot_be_empty}")
    @NotNull(message = "{validation.user.password_is_mandatory}")
    @Size(max = 32, message = "{validation.user.password_is_too_long}")
    private String password;

    @NotBlank(message = "{validation.user.email_cannot_be_empty}")
    @NotNull(message = "{validation.user.email_is_mandatory}")
    @Size(max = 250, message = "{validation.user.email_is_too_long}")
    @Email(message = "{validation.user.email_is_invalid}")
    private String email;

    @NotBlank(message = "{validation.user.phone_cannot_be_empty}")
    @NotNull(message = "{validation.user.phone_is_mandatory}")
    @Size(max = 50, message = "{validation.user.phone_is_too_long}")
    @E164PhoneValidation
    private String phone;

    @NotBlank(message = "{validation.user.country_code_cannot_be_empty}")
    @NotNull(message = "{validation.user.country_code_is_mandatory}")
    @Length(min = 2, max = 2, message = "{validation.user.country_code_is_invalid}")
    @JsonProperty("country_code")
    @CountryValidation
    private String countryCode;

    @NotBlank(message = "{validation.user.first_name_is_mandatory}")
    @NotNull(message = "{validation.user.first_name_is_mandatory}")
    @Size(max = 250, message = "{validation.user.first_name_is_too_long}")
    @JsonProperty("first_name")
    private String firstName;

    @NotBlank(message = "{validation.user.last_name_is_mandatory}")
    @NotNull(message = "{validation.user.last_name_is_mandatory}")
    @Size(max = 250, message = "{validation.user.last_name_is_too_long}")
    @JsonProperty("last_name")
    private String lastName;

}
