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
public class TaskFieldsToEdit {

    @NotBlank(message = "Empty first name")
    @Size(max = 50, message = "Too long title")
    private String title;

    @Size(max = 255, message = "Too long description")
    private String description;
}
