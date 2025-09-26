// UserDto.java
package com.example.HW2.client_processing.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private Long id;
    private String login;
    private String email;
}
