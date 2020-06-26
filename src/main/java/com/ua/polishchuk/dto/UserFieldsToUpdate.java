package com.ua.polishchuk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserFieldsToUpdate {

    @NotBlank(message = "Empty first name")
    @Size(max = 50, message = "Too long first name")
    private String firstName;

    @NotBlank(message = "Empty last name")
    @Size(max = 50, message = "Too long last name")
    private String lastName;

    @NotBlank(message = "Empty role")
    private String role;

    //removed password and email because it should be separate functionality
}
