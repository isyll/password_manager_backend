package com.isyll.password_manager.dto.mapper;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.isyll.password_manager.dto.payload.request.SignUpRequest;
import com.isyll.password_manager.dto.payload.request.UpdateUserRequest;
import com.isyll.password_manager.models.User;

@Mapper(componentModel = "spring", imports = { ZonedDateTime.class, DateTimeFormatter.class })
public interface UserMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateUserFromUpdateRequest(UpdateUserRequest data, @MappingTarget User entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateUserFromSignupRequest(SignUpRequest data, @MappingTarget User entity);

    default ZonedDateTime mapStringToZonedDateTime(String date) {
        if (date == null || date.isBlank()) {
            return null;
        }
        return ZonedDateTime.parse(date, DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }
}
